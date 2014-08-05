#!/usr/bin/env python
import os
import sqlite3
import buildResponse

print 'Content-type: application/json'
print

def main():
    with sqlite3.connect('../' + os.environ['databaseFile']) as conn:
        conn.row_factory = sqlite3.Row
        c = conn.cursor()

        c.execute('select * from players')

        players = c.fetchall()

        response = map(lambda x: dict(zip(x.keys(), x)), players)

        print buildResponse.build(True, response)

if __name__ == '__main__':
    main()
