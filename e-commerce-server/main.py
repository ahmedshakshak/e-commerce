from app import *
from app.db import  db


@app.before_first_request
def create_tables():
    db.drop_all()
    db.create_all()
    print('tables has been created')

if __name__ == '__main__':
    app.run()


