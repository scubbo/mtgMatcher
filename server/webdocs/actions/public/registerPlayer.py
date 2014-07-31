#!/usr/bin/python
import cgi
import json
import sqlite3
import datetime

import cgitb
cgitb.enable()

def generateSecretToken():
    return 'abc'

def main():
    print "Content-type: application/json"
    print

    form = cgi.FieldStorage()

    REQUIRED_FIELDS = ['name','dciNumber','regId']

    for field in REQUIRED_FIELDS:
        if field not in form:
            response = {'status':'error','message':'required field ' + field + ' was missing from the request'}
            print json.dumps(response)
            return

    with sqlite3.connect('../data.db') as conn:
        conn.row_factory = sqlite3.Row
        c = conn.cursor()

        try:
            rid = form['regId'].value
            existingPlayer = c.execute("SELECT * FROM players WHERE regId=?", (rid,)).next()

            response = {
                'status':'failure',
                'code':'alreadyRegistered',
                'message':'already registered',
                'data': {
                    'name':existingPlayer['name'],
                    'dciNumber':existingPlayer['dciNumber']
                }
            }

            print json.dumps(response)
        except StopIteration:

            secretToken = generateSecretToken()
            c.execute("INSERT INTO players VALUES (?,?,?,?,?)", (form['name'].value, form['dciNumber'].value, form['regId'].value, secretToken, datetime.datetime.now()))

            conn.commit()
            id = c.execute('select count(*) from players').fetchone()[0]

            response = {
                'status':'success',
                'data':{
                    'id':id,
                    'secretToken':secretToken
                }
            }

            print json.dumps(response)

if __name__ == '__main__':
    main()
