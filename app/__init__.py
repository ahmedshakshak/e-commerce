from flask import Flask, Blueprint
from flask_restful import Api
from flask_jwt_extended import JWTManager

app = Flask(__name__)
app.config['SECRET_KEY'] = 'ThisIsHardestThing'
app.config['JWT_SECRET_KEY'] = 'Dude!WhyShouldYouEncryptIt'
app.config['JWT_BLACKLIST_ENABLED'] = True
app.config['JWT_BLACKLIST_TOKEN_CHECKS'] = ['access', 'refresh']

jwt = JWTManager(app)

api_bp = Blueprint('api', __name__, url_prefix='/api')
api = Api(api_bp)
app.register_blueprint(api_bp)

from app.routes import *
