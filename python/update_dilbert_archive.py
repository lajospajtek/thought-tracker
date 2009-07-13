#!/usr/bin/python
################################################################

proxies = {
    #'http': 'http://www-proxy:80',
}

local_fn_format = '%Y_%m_%d'
#local_fn_format = 'dilbert%Y%m%d'
where_to = '/path/to/dilbert/archive/'
number_of_days = 30

################################################################

import sys
import urllib
import re
import os

from datetime import date, timedelta

################################################################

def output_file_path(date):
    file_name = date.strftime(local_fn_format) + '.gif'
    return os.path.normpath(os.path.join(where_to, file_name))

def get_html_url(date):
    return date.strftime('http://www.dilbert.com/fast/%Y-%m-%d/')
    
def find_strip_url(content, args):
    pattern = re.compile('/dyn/str_strip/.*strip.print.gif')
    for line in content:
        match = pattern.search(line)
        if match != None:
            result = 'http://www.dilbert.com/' + match.group()
            break
    else:
        #print 'strip not available'
        result = None
    return result

def save(content, output_file_path):
    disk_file = file(output_file_path, 'wb')
    disk_file.write(content.read())
    disk_file.close()

def with_open_url_do(url, fun, args = None):
    content = urllib.urlopen(url, proxies = proxies)
    result = fun(content, args)
    content.close()
    return result

def get_strip(date):
    print date , 
    html_url = get_html_url(date)
    image_url = with_open_url_do(html_url, find_strip_url)
    print image_url, 
    if image_url != None: 
        file_path = output_file_path(date)
        with_open_url_do(image_url, save, output_file_path(date))
        print file_path
    else: 
        print None

def file_missing_for_date(date):
    return not os.path.exists(output_file_path(date))

def update_dilbert_archive():
    last_thirty_days = [date.today()+timedelta(delta) for delta in range(1-number_of_days, 1)]
    days_missing_strips = filter (file_missing_for_date, last_thirty_days)
    for day in days_missing_strips: get_strip(day)

update_dilbert_archive()

