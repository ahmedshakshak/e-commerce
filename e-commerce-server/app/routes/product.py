from flask_restful import Resource, reqparse, marshal_with , fields
from flask_jwt_extended import jwt_required
from flask import request
import json

from app.db.category import *
from app.db.product import *
from app import api

product_fields = {
    'id': fields.String(attribute=lambda x: x.id),
    'name': fields.String(attribute=lambda x: x.name),
    'price': fields.String(attribute=lambda x: x.price),
    'quantity': fields.String(attribute=lambda x: x.quantity),
    'category_name': fields.String(attribute=lambda x: x.category.name),
    'category_id': fields.String(attribute=lambda x: x.category.id)
}

product_filter_parser = reqparse.RequestParser()
product_filter_parser.add_argument('name', type=str, required=False)
product_filter_parser.add_argument('category', type=str, required=False)


product_array_parser = reqparse.RequestParser()
product_array_parser.add_argument('array', type=dict, action="append")


class ProductsResource(Resource):
    @marshal_with(product_fields)
    def get(self):
        print(request.headers,request.data, request.form)
        products = product_filter_parser.parse_args()
        query = Product.query.outerjoin(Category).all()
        print(products['name'])
        if products['name']:
            query = Product.find(name=products['name'])

        if products['category']:
            query = Product.find(name=products['category'])
        return query

    @jwt_required
    def post(self):
        products = product_array_parser.parse_args()['array']
        queries = []
        print(products, type(products))
        for product in products:
            id = product['id']
            quantity = product['quantity']
            query = Product.query.get(id)
            queries.append(query)
            print(query.quantity, quantity)
            if query and query.quantity >= quantity:
                query.quantity -= quantity
            else:
                return {'message': 'Out Of Stock'}

        for query in queries:
            query.save()

        return {'message': 'Ordered successfully'}




api.add_resource(ProductsResource, '/products')
