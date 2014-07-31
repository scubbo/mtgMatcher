#!/usr/bin/env python
import json
import os

print 'Content-type: application/json'
print

def main():
    print json.dumps(dict(os.environ))

if __name__ == '__main__':
    main()
