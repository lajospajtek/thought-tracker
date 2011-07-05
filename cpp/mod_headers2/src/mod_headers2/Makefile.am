SUBDIRS = . unit

pkglib_LTLIBRARIES = mod_headers2.la
pkglibdir = $(libdir)/apache2/modules
mod_headers2_la_SOURCES =  \
	Headers2Conf.h     \
	mod_headers2.c

mod_headers2_la_LDFLAGS = -version-info 1:0:0 -module -rpath $(libdir)/apache2/modules `apxs2 -q LDFLAGS`

mod_headers2_la_LIBADD = -laprutil-1 -lapr-1

mod_headers2_la_CFLAGS = -D_GNU_SOURCE=1 `apr-config --includes` -I`apxs2 -q INCLUDEDIR`