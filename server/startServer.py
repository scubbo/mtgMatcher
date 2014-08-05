#!/usr/bin/env python
import argparse

import BaseHTTPServer

import os


def main():
    # Set extra paths
    try:
        originalPythonPath = os.environ['PYTHONPATH']
    except KeyError:
        # No path set originally
        originalPythonPath = ''

    mainProjectDir = os.path.dirname(os.path.abspath(__file__)) + os.path.sep
    os.chdir(mainProjectDir + 'webdocs')
    newPythonPath = ':'.join(originalPythonPath.split(':') + [ mainProjectDir + 'webdocs/scripts'])
    os.environ['PYTHONPATH'] = newPythonPath

    import sys
    sys.path.append(mainProjectDir + 'customServer') #Have to do this because sys.path is not refreshed when $PYTHONPATH changes
    import ExtendedCGIHTTPServer

    parser = argparse.ArgumentParser(description="Start the server for mtgMatcher")
    parser.add_argument('--port', default=2020, type=int)
    parser.add_argument('--gcmUrl', default='https://android.googleapis.com/gcm/send')
    parser.add_argument('--databaseFile', default='data.db')
    args = parser.parse_args()

    if not os.path.exists(mainProjectDir + args.databaseFile):
        print 'Woah there, cowboy! "' + args.databaseFile + '" doesn\'t even exist! Are you sure you\'ve run install.py?'
        return

    server = BaseHTTPServer.HTTPServer
    handler = ExtendedCGIHTTPServer.ExtendedCGIHTTPRequestHandler
    server_address = ("", args.port)
    handler.cgi_directories = ["/actions"]

    os.environ['gcmUrl'] = args.gcmUrl
    os.environ['databaseFile'] = args.databaseFile

    httpd = server(server_address, handler)
    httpd.serve_forever()

if __name__ == '__main__':
    main()