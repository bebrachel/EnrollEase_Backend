from ..Google import *


def exec(companion):
    show_chatty_threads(companion)


if __name__ == "__main__":
    import sys

    args = sys.argv[1:]
    if len(args) != 1:
        print("Usage: python script.py <user_gmail_adress>")
        exit(1)
    companion = args[0]
    exec(companion)
