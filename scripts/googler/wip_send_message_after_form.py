from Google import *

s = ""
with open('send_message/templates/ПортфолиоСоздано.txt') as openfileobject:
    s = openfileobject.read()

gmail_create("НГУ Магистратура", s, ['<EMAIL>', '<EMAIL>'])
