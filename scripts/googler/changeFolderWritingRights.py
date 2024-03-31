from Google import give_user_permission

if __name__ == '__main__':
    import sys

    if len(sys.argv) > 3:
        email = sys.argv[1]
        folder = sys.argv[2]
        role = sys.argv[3]
        give_user_permission(folder, email, role, updated=True)
