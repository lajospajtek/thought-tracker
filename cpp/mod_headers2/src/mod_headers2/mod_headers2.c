#include <httpd.h> // server_rec
#include <http_config.h> // module struct
#include <http_request.h> // hook_fixups
#include <http_log.h>
#include <util_filter.h>
#include <apr_pools.h>
#include <apr_buckets.h>
#include <apr_tables.h>
#include <apr_strings.h>
#include <stdlib.h>
#include "Headers2Conf.h"

extern module AP_MODULE_DECLARE_DATA headers2_module;

static ap_filter_rec_t *hdrs2_out;
static ap_filter_rec_t *hdrs2_err;

typedef struct HeaderPrivate_ HeaderPrivate;

struct HeaderPrivate_ {
	const HeaderExtConf *hdr;
	request_rec *r;
	apr_table_t *hdrs_out;
};

static int
copy_edit_hdr(HeaderPrivate *d, const char *key, const char *value) {
	const HeaderExtConf *hdr = d->hdr;
	if (NULL != hdr->cond) {
		if ('!' == *hdr->cond) {
			if (NULL != apr_table_get(d->r->subprocess_env, hdr->cond + 5))
				return 1;
		} else {
			if (NULL == apr_table_get(d->r->subprocess_env, hdr->cond + 4))
				return 1;
		}
	}
	ap_regmatch_t match[AP_MAX_REG_MATCH];
	if (0 != ap_regexec(&hdr->regex, value, AP_MAX_REG_MATCH, match, 0))
		return 1;
	char *new_hdr = ap_pregsub(d->r->pool, hdr->val, value, hdr->regex.re_nsub + 1, match);
	apr_table_addn(d->hdrs_out, key, new_hdr);
	return 1;
}

static int
addn(apr_table_t *t, const char *key, const char *value) {
	apr_table_addn(t, key, value);
	return 1;
}

static void
copy_edit(request_rec *r, const HeaderExtConf *hdr, apr_table_t *hdrs) {
	HeaderPrivate d;
	d.hdr = hdr;
	d.r = r;
	d.hdrs_out = apr_table_make(r->pool, 4);
	apr_table_do((int (*)(void *, const char *, const char *))&copy_edit_hdr, &d, hdrs, hdr->hdr, NULL);
	if (apr_is_empty_table(d.hdrs_out))
		return;
	apr_table_do((int (*)(void *, const char *, const char *))&addn, hdrs, d.hdrs_out, NULL);
}

static void
apply_hdrs2_rule(request_rec *r, const HeaderExtConf *hdr, apr_table_t *hdrs) {
	switch (hdr->cmd) {
	case COPY_EDIT:
		copy_edit(r, hdr, hdrs);
		break;
	default:
		break;
	}
}

static void
apply_hdrs2_rules(request_rec *r, const apr_array_header_t *cnf, apr_table_t *hdrs) {
	const HeaderExtConf *hdr = (HeaderExtConf *)cnf->elts;
	int i;
	for (i = 0; i < cnf->nelts; ++i)
		apply_hdrs2_rule(r, hdr + i, hdrs);
}

//static int
//do_hdrs2(request_rec *r) {
//	hdrs2_rules(r, r->headers_out);
//	hdrs2_rules(r, r->err_headers_out);
//	return OK;
//}

static void
insert_flt(request_rec *r, ap_filter_rec_t *f, const char *msg) {
	if (NULL == ap_add_output_filter_handle(f, NULL, r, r->connection))
		ap_log_rerror(APLOG_MARK, APLOG_CRIT, APR_ENOMEM, r, "mod_headers2: Error adding %s filter.", msg);
}

static void
insert_out_flt(request_rec *r) {
	insert_flt(r, hdrs2_out, "output");
}

static void
insert_err_flt(request_rec *r) {
	insert_flt(r, hdrs2_err, "error");
}

static apr_status_t
hdrs2_err_flt(ap_filter_t *f, apr_bucket_brigade *bb) {
	ap_filter_t *nxt = f->next;
	Headers2Conf *cnf = (Headers2Conf *)ap_get_module_config(f->r->per_dir_config, &headers2_module);
	if (NULL == cnf) {
		ap_log_rerror(APLOG_MARK, APLOG_CRIT, APR_ENOMEM, f->r, "mod_headers2: No Headers2Conf object");
		return APR_ENOMEM;
	}
	apply_hdrs2_rules(f->r, cnf->err_rules, f->r->err_headers_out);
	ap_remove_output_filter(f);
	return ap_pass_brigade(nxt, bb);
}

static apr_status_t
hdrs2_out_flt(ap_filter_t *f, apr_bucket_brigade *bb) {
	ap_filter_t *nxt = f->next;
	Headers2Conf *cnf = (Headers2Conf *)ap_get_module_config(f->r->per_dir_config, &headers2_module);
	if (NULL == cnf) {
		ap_log_rerror(APLOG_MARK, APLOG_CRIT, APR_ENOMEM, f->r, "mod_headers2: No Headers2Conf object");
		return APR_ENOMEM;
	}
	apply_hdrs2_rules(f->r, cnf->err_rules, f->r->err_headers_out);
	apply_hdrs2_rules(f->r, cnf->out_rules, f->r->headers_out);
	ap_remove_output_filter(f);
	return ap_pass_brigade(nxt, bb);
}

static void
register_hooks(apr_pool_t *p) {
	static const char *headers_pred[] = {"mod_headers.c", NULL};
//	ap_hook_fixups(&do_hdrs2, headers_pred, NULL, APR_HOOK_MIDDLE);

	hdrs2_out = ap_register_output_filter("hdrs2_out_flt", &hdrs2_out_flt, NULL, AP_FTYPE_CONTENT_SET);
	if (NULL == hdrs2_out) {
		ap_log_perror(APLOG_MARK, APLOG_CRIT, APR_ENOMEM, p, "%s", "mod_headers2: Error registering output filter.");
		exit(1);
	}

	hdrs2_err = ap_register_output_filter("hdrs2_err_flt", &hdrs2_err_flt, NULL, AP_FTYPE_CONTENT_SET);
	if (NULL == hdrs2_err) {
		ap_log_perror(APLOG_MARK, APLOG_CRIT, APR_ENOMEM, p, "%s", "mod_headers2: Error registering error filter.");
		exit(1);
	}

	ap_hook_insert_filter(&insert_err_flt, headers_pred, NULL, APR_HOOK_LAST);
}

static apr_status_t
free_hdrs(HeaderExtConf *hdr) {
	ap_regfree(&hdr->regex);
	return APR_SUCCESS;
}

static const char *
hdrs2_conf(cmd_parms *params, Headers2Conf *cnf, int argc, char *const argv[]) {
	if (argc < 4)
		return "Too few arguments to HeaderExt";
	int ndx = 0;
	int always = 0 == strcasecmp(argv[ndx], "always");
	HeaderExtConf *hdr = (HeaderExtConf *)(always ? apr_array_push(cnf->err_rules) : apr_array_push(cnf->out_rules));
	if (always || 0 == strcasecmp(argv[ndx], "onsuccess")) {
		if (argc < 5)
			return "Too few arguments to HeaderExt";
		++ndx;
	}
	// ndx == 0 or 1
	if (0 == strcasecmp(argv[ndx], "copyedit")) {
		hdr->cmd = COPY_EDIT;
		++ndx;
//	} else if (0 == strcasecmp(argv[ndx], "")) {
//		++ndx;
//	} else if (0 == strcasecmp(argv[ndx], "")) {
//		++ndx;
//	} else if (0 == strcasecmp(argv[ndx], "")) {
//		++ndx;
	} else
		return "HeaderExt command must be copyedit";
	// ndx == 1 or 2
	hdr->hdr = argv[ndx++];
	// ndx == 2 or 3
	if (0 != ap_regcomp(&hdr->regex, argv[ndx], 0))
		return apr_psprintf(params->pool, "Invalid regex: %s", argv[ndx]);
	apr_pool_cleanup_register(params->pool, hdr, (apr_status_t (*)(void *))&free_hdrs, &apr_pool_cleanup_null);
	++ndx;
	// ndx == 3 or 4
	hdr->val = argv[ndx++];
	// ndx == 4 or 5
	if (ndx < argc) {
		if (0 != strncasecmp(argv[ndx] + ('!' == *argv[ndx]), "env=", 4))
			return "Condition must contain [!]env=";
		hdr->cond = argv[ndx++];
	} else
		hdr->cond = NULL;
	// ndx == 4 or 5 or 6
	if (ndx < argc)
		return "Too many arguments to HeaderExt";
	return NULL;
}

static Headers2Conf *
create_config(apr_pool_t *p) {
	Headers2Conf *cnf = (Headers2Conf *)apr_pcalloc(p, sizeof(Headers2Conf));
	cnf->out_rules = apr_array_make(p, 32, sizeof(HeaderExtConf));
	cnf->err_rules = apr_array_make(p, 32, sizeof(HeaderExtConf));
	return cnf;
}

static Headers2Conf *
merge_config(apr_pool_t *p, const Headers2Conf *parent, Headers2Conf *child) {
	Headers2Conf *cnf = (Headers2Conf *)apr_pcalloc(p, sizeof(Headers2Conf));
	cnf->out_rules = apr_array_append(p, parent->out_rules, child->out_rules);
	cnf->err_rules = apr_array_append(p, parent->err_rules, child->err_rules);
	return cnf;
}

static command_rec cmds[] = {
	AP_INIT_TAKE_ARGV("HeaderExt",
		(const char *(*)())&hdrs2_conf,
		NULL,
		OR_FILEINFO,
		"Extension to the Header directive. "
		"Format: HeaderExt [always|onsuccess] copyedit header regexp replacement [condition] "
		"Example: HeaderExt copyedit Set-Cookie name=(.*)domain=[^;]+(.*) name=$1domain=mydomain.com$2 !env=error"
	),
	{NULL}
};

module AP_MODULE_DECLARE_DATA headers2_module = {
	STANDARD20_MODULE_STUFF,
	(void *(*)(apr_pool_t *, char *))&create_config,// create_dir_config
	(void *(*)(apr_pool_t *, void *, void *))&merge_config,// merge_dir_config
	NULL,// create_server_config
	NULL,// merge_server_config
	cmds,// cmds
	register_hooks
};
