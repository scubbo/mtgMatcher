"""Extension to CGIHTTPServer

This has two main motivations:

# Allows more customisable cgi_directories (more than just a single top-level directory - e.g. ['/cgi-bin','/something/somethingelse']
# Allows cgi_directories to be restricted per host (not yet actually implemented) TODO
"""
import CGIHTTPServer

class ExtendedCGIHTTPRequestHandler(CGIHTTPServer.CGIHTTPRequestHandler):

    def is_cgi(self):
        collapsed_path = CGIHTTPServer._url_collapse_path(self.path)
        dir_sep = collapsed_path.find('/', 1)
        head, tail = collapsed_path[:dir_sep], collapsed_path[dir_sep+1:]
        if head in self.cgi_directories:
            self.cgi_info = head, tail
            return True
        return False

    private_cgi_directories = [] #CGI directories that can only be called locally - implement this
