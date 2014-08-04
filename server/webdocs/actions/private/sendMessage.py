#!/usr/bin/python
import requests
import cgi
import json
import os

sendURL = os.environ['gcmUrl']
with file('../apiKey','r') as f:
    apiKey = f.read().rstrip()

def buildRequest(regIds, message):
    headers = {
        'Content-Type': 'application/json',
        'Authorization': 'key='+apiKey
    }

    data = {
        'registration_ids': regIds,
        'data': {
            'message': message
        }
    }

    r = requests.post(sendURL, data=json.dumps(data), headers=headers)

    return r.text

def main():
    print 'Content-type: application/json'
    print

    form = cgi.FieldStorage()

    if 'regIds' not in form and 'regId' not in form:
        response = {
            'status':'failure',
            'reason':'request did not contain required fields "regId" or "regIds"'
        }
        print json.dumps(response)
        return

    if 'regIds' not in form:
        regIds = [form['regId'].value]
    else:
        regIds = form['regId'].value.split(',')

    if 'message' not in form:
        response = {
            'status':'failure',
            'reason':'request did not contain required field "message"'
        }
        print json.dumps(response)
        return

    response = {
        'status':'success',
        'data':buildRequest(regIds, form['message'].value)
    }

    print json.dumps(response)

if __name__ == '__main__':
    main()
