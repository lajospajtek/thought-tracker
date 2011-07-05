#ifndef __Headers2Conf__h__
#define __Headers2Conf__h__

#include <apr_tables.h>
#include <ap_regex.h>

typedef enum {COPY_EDIT} HdrCmd;

typedef struct HeaderExtConf_ HeaderExtConf;
struct HeaderExtConf_ {
	HdrCmd cmd;
	const char *hdr;
	ap_regex_t regex;
	const char *val;
	const char *cond;
};

typedef struct Headers2Conf_ Headers2Conf;
struct Headers2Conf_ {
	apr_array_header_t *err_rules;
	apr_array_header_t *out_rules;
};


#endif
