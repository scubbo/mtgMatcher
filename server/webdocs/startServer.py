#!/usr/bin/env python
import argparse

import BaseHTTPServer
import CGIHTTPServer
import cgitb

import os

cgitb.enable()  # Error reporting

import os, sys
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + os.path.sep + os.path.pardir + os.path.sep + 'customServer')

import ExtendedCGIHTTPServer

parser = argparse.ArgumentParser(description="Start the server for mtgMatcher")
parser.add_argument('--port', default=2020, type=int)
parser.add_argument('--gcmUrl', default='https://android.googleapis.com/gcm/send')
args = parser.parse_args()

server = BaseHTTPServer.HTTPServer
handler = ExtendedCGIHTTPServer.ExtendedCGIHTTPRequestHandler
server_address = ("", args.port)
handler.cgi_directories = ["/actions"]

os.environ['gcmUrl'] = args.gcmUrl

httpd = server(server_address, handler)
httpd.serve_forever()
