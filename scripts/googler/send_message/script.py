from __future__ import print_function
from ..Google import *


def create(subject, content, recipients):
    gmail_create(subject, content, recipients)


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    if len(args) != 3:
        print('Usage: python script.py <subject> <body> <["recipient"*]>')
        exit(1)

    try:
        from ast import literal_eval

        gmail_create(args[0], args[1], literal_eval(args[2]))
    except ValueError as err:
        print(f'Recipients cannot be parsed\n'
              f'Format must be like ["lolipop@gmail.com", "blacksparrow@ramble.rr"')
