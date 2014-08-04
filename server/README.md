mtgMatcherServer
================

Usage:

`sudo ./install.py`
This will set up the required database

`cd webdocs`
`./startServer.py`
This will start the server. By default, it runs on port 2020. If you wish to pass a different port, use `./startServer.py --port <port>`

Run with a different gcmUrl (e.g. for testing) by passing `--gcmUrl <url>`

NOTE TO SELF - submit pull request to update BaseHTTPServer, line 266, hack for w.exe

This is a meaningless update to test subtree merge pulling (in main repo)
