CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE commands (
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    command VARCHAR NOT NULL,
    "text" VARCHAR NOT NULL,
    "userId" VARCHAR NOT NULL,
    "userName" VARCHAR NOT NULL,
    "teamId" VARCHAR NOT NULL,
    "teamDomain" VARCHAR NOT NULL,
    "responseUrl" VARCHAR NOT NULL,
    "createdAt" TIMESTAMP NOT NULL
);

CREATE TABLE haikus (
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    haiku VARCHAR NOT NULL,
    language VARCHAR(3) NOT NULL,
    "commandId" UUID NOT NULL REFERENCES commands (id),
    "createdAt" TIMESTAMP NOT NULL
);

CREATE TABLE haiku_events (
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    "haikuId" UUID NOT NULL REFERENCES haikus (id),
    "action" VARCHAR NOT NULL,
    "value" VARCHAR NOT NULL,
    "createdAt" TIMESTAMP NOT NULL
);
