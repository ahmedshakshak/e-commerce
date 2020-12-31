from app import api, jwt
from flask_restful import inputs, reqparse, Resource
from app.db.customer import *
from datetime import datetime
from flask_jwt_extended import (
    create_access_token,
    create_refresh_token,
    jwt_required,
    get_raw_jwt,
    jwt_refresh_token_required,
    get_jwt_identity,
)

login_parser = reqparse.RequestParser()
login_parser.add_argument('username', required=True)
login_parser.add_argument('password', required=True)

register_parser = reqparse.RequestParser()
register_parser.add_argument('first_name', required=True)
register_parser.add_argument('last_name', required=True)
register_parser.add_argument('username', required=True)
register_parser.add_argument('email', required=True)
register_parser.add_argument('birthdate', type=lambda x: datetime.strptime(x,'%Y-%m-%d'), required=True)
register_parser.add_argument('gender', required=True)
register_parser.add_argument('job', required=True)
register_parser.add_argument('password', required=True)


class Register(Resource):
   def post(self):
       print('registering:')
       customer = register_parser.parse_args()
       username = customer['username']
       email = customer['email']
       print(customer)
       if Customer.find_by_username(username):
           return {'message': f'User {username} already exists'}
       if Customer.find_by_email(email):
           return {'message': f'email {email} already exists'}

       first_name = customer['first_name']
       last_name = customer['last_name']
       password = customer['password']
       gender = customer['gender']
       birthdate = customer['birthdate']
       job = customer['job']
       new_customer = Customer(first_name=first_name, last_name=last_name, job=job, username=username, email=email,
                               password=Customer.generate_hash(password), gender=gender, birthdate=birthdate)
       new_customer.save()
       access_token = create_access_token(identity=username)
       refresh_token = create_refresh_token(identity=username)
       return {
           'message': f'User {username} was created',
           'access_token': access_token,
           'refresh_token': refresh_token
       }


class Login(Resource):
    def post(self):
        customer = login_parser.parse_args()
        username = customer['username']
        password = customer['password']
        customer = Customer.find_by_username(username)

        if not customer:
            return {'message': f'User {username} doesn\'t exist'}

        if Customer.verify_hash(password, customer.password):
            access_token = create_access_token(identity=username)
            refresh_token = create_refresh_token(identity=username)
            return {
                'message': f'Logged in as {username}',
                'access_token': access_token,
                'refresh_token': refresh_token
            }
        else:
            return {'message': "Wrong credentials"}


class Logout(Resource):
    @jwt_required
    def post(self):
        jti = get_raw_jwt()['jti']
        revoked_token = RevokedTokenModel(jti=jti)
        revoked_token.save()
        return {'message': 'Access token has been revoked'}


class TokenRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        current_user = get_jwt_identity()
        access_token = create_access_token(identity=current_user)
        return {'access_token': access_token}


@jwt.token_in_blacklist_loader
def token_in_blacklist(decrypted_token):
    jti = decrypted_token['jti']
    return RevokedTokenModel.is_jti_blacklisted(jti)

api.add_resource(Register, '/register');
api.add_resource(Login, '/login');
api.add_resource(Logout, '/logout');
api.add_resource(TokenRefresh, '/refresh');
