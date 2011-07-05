AC_INIT([libapache2-mod-headers2], [1.0.0], [bug-automake@gnu.org])
AM_INIT_AUTOMAKE([-Wall -Werror foreign])

AC_PROG_CC

AC_ENABLE_STATIC(no)
AC_CONFIG_MACRO_DIR([m4])
AC_PROG_LIBTOOL

AC_ARG_ENABLE(debug, [AS_HELP_STRING([--enable-debug], [enable debug @<:@default=no@:>@])], [ if test "${enable_debug:-no}" != no; then CFLAGS="-g -O0 -fno-inline"; fi ], [CFLAGS="-O2"])

AC_PREFIX_DEFAULT([/usr])
AC_CONFIG_COMMANDS_PRE([
if test "$sysconfdir" = '${prefix}/etc'; then sysconfdir=/etc; fi;
if test "$localstatedir" = '${prefix}/var'; then localstatedir=/var; fi
])

AC_CONFIG_HEADERS([config.h])
AC_CONFIG_FILES([
	Makefile
	conf/Makefile
	src/Makefile
	src/mod_headers2/Makefile
	src/mod_headers2/unit/Makefile
])

AC_OUTPUT
