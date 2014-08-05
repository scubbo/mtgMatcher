#!/usr/bin/env python
import argparse
import os

def main():
    import platform
    import os.path

    if platform.system() == 'Windows':
        print 'Detected that you are running Windows. mtgMatcher was developed on Mac and tested on Linux - correct operation is not guaranteed on Windows machines.'

    parser = argparse.ArgumentParser(description="Installs dependencies and initializes database for server")
    parser.add_argument('--databaseFile', default='data.db')
    parser.add_argument('--databaseOnly', dest='databaseOnly', action='store_true')
    parser.add_argument('--no-databaseOnly', dest='databaseOnly', action='store_false')
    args = parser.parse_args()

    if os.geteuid() != 0 and not args.databaseOnly:
        exit("install.py needs to be run as root")

    if args.databaseOnly:
        try:
            import pip
        except ImportError:
            print 'If you don\'t have pip installed, you\'re going to have a bad time.'
            print 'You can try to download it yourself from http://pip.readthedocs.org/en/latest/installing.html, or this script can install it for you'
            installPip()
            import pip


    if not os.path.exists(args.databaseFile):
        setupDatabase(args.databaseFile)
        print 'Database created'
    else:
        print 'ERROR: Database file "' + args.databaseFile + '" already exists'
        return

    if args.databaseOnly:
        try:
            import qrcode
        except ImportError:
            yn = raw_input('Optional module qrcode is not installed - install it? [y/n] ')
            if yn == 'y':
                pip.main(['install','qrcode'])
                import qrcode

    print 'Installation complete. Start the server with startServer.py'

def installPip():
    import urllib2
    url = 'https://bootstrap.pypa.io/get-pip.py'

    file_name = url.split('/')[-1]
    u = urllib2.urlopen(url)
    f = open(file_name, 'wb')
    meta = u.info()
    file_size = int(meta.getheaders("Content-Length")[0])
    print "Downloading: %s Bytes: %s" % (file_name, file_size)

    file_size_dl = 0
    block_sz = 8192
    while True:
        buffer = u.read(block_sz)
        if not buffer:
            break

        file_size_dl += len(buffer)
        f.write(buffer)
        status = r"%10d  [%3.2f%%]" % (file_size_dl, file_size_dl * 100. / file_size)
        status = status + chr(8)*(len(status)+1)
        print status,

    f.close()

    os.system(file_name)

def setupDatabase(databaseFileName):
    try:
        import sqlite3
    except ImportError:
        print 'Essential module sqlite3 is not installed - downloading...'
        pip.main(['install','sqlite3'])
        import sqlite3

    file(databaseFileName,'w').close()
    os.chmod(databaseFileName,0o660)

    conn = sqlite3.connect(databaseFileName)
    c = conn.cursor()

    c.execute('CREATE TABLE players (name text, dciNumber text, regId text, secretToken text, regTime timestamp)')

    conn.commit()
    conn.close()

if __name__=='__main__':
    main()
