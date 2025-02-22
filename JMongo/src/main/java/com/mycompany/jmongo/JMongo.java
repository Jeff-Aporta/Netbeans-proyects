package com.mycompany.jmongo;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class JMongo {

    public static void main(String[] args) {
        String uri_string = "mongodb+srv://udt:udtccsso@cluster0.fceho.mongodb.net/";
        try ( MongoClient mongoClient = MongoClients.create(uri_string)) {
            MongoDatabase database = mongoClient.getDatabase("test");
            try {
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                Document commandResult = database.runCommand(command);
                System.out.println("Connected successfully to server.");
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
        }

    }
}
