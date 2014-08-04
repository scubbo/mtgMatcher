#!/usr/bin/env python
import json

def build(isSuccess, data = None, code = None):
    returnObject = {}
    if isSuccess:
        returnObject['status'] = 'success'
        returnObject['data'] = data
    else:
        returnObject['status'] = 'failure'
        if code: returnObject['code'] = code
        if data: returnObject['data'] = data
    return json.dumps(returnObject)