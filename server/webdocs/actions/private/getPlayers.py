#!/usr/bin/env python
import json
import sqlite3

print 'Content-type: application/json'
print

def main():
    with sqlite3.connect('../data.db') as conn:
        conn.row_factory = sqlite3.Row
        c = conn.cursor()

        c.execute('select * from players')

        players = c.fetchall()

        response = map(lambda x: dict(zip(x.keys(), x)), players)

        print json.dumps(response)

if __name__ == '__main__':
    main()
