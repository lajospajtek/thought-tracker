#!/usr/bin/python
################################################################

proxies = {
    #'http': 'http://www-proxy:80',
}

where_to = 'x:/backup/dilbert_archive/'

number_of_days = 30
################################################################

import sys
import urllib
import re
import os

from string import join
from datetime import datetime, timedelta


def dilbert_html_url(date):
    html_url = 'http://www.dilbert.com/comics/dilbert/archive/dilbert-%s.html'
    return html_url % (date.strftime('%Y%m%d'))

def dilbert_image_url(filename):
    image_url = 'http://www.dilbert.com/comics/dilbert/archive/images/%s'
    return image_url % (filename)

def find_file_name(content, args):
    pattern = re.compile('dilbert\\d+\\.(gif|jpg)')
    file_name = None
    extension = None
    for line in content:
        match = pattern.search(line)
        if match != None:
            file_name = match.group()
            extension = match.group(1)
            break
    return [file_name, extension]

def with_open_url_do(url, fun, args = None):
    content = urllib.urlopen(url, proxies = proxies)
    result = fun(content, args)
    content.close()
    return result

def write_image_to_disk(content, output_file_path):
    disk_file = file(output_file_path, 'wb')
    disk_file.write(content.read())
    disk_file.close()

def output_file_path(where_to, date, extension):
    result = None
    if extension != None:
      file_name = date.strftime('%Y_%m_%d') + '.' + extension
      result = os.path.normpath(os.path.join(where_to, file_name))
    return result

def file_exists(file_name):
    return os.path.exists(file_name)

def download_dilbert_strip(args):
        remote_file_name, output_file = args

        if remote_file_name == None:
            print '- strip not found'
            return

        if output_file == None:
            print ' - cannot build output file'
            return

        if file_exists(output_file):
            print '= %s already exists' % (output_file)
            return


        url = dilbert_image_url(remote_file_name)
        print '+ %s -> %s' % (url, output_file)
        with_open_url_do(url, write_image_to_disk, output_file)

def find_dilbert_strip(date):
    url = dilbert_html_url(date)
    print '? %s' % (url)
    file_name, extension = with_open_url_do(url, find_file_name)
    output_file = output_file_path(where_to, date, extension)
    return [file_name, output_file]

def println():
    print ''

def get_last_strips(number_of_days):
    date = datetime.today()
    one_day = timedelta(1)

    for i in range(number_of_days):
        println()
        download_dilbert_strip(find_dilbert_strip(date))
        println()
        date = date - one_day


################################################################
get_last_strips(number_of_days)


