OAUTH2_CLIENT_ID ?= testclientid
OAUTH2_CLIENT_SECRET ?= testlcientsecret
OAUTH2_CALLBACK_URL ?= http://localhost/auth

kibit:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test kibit

eastwood:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test eastwood \
	"{:namespaces [:source-paths]}"

lint: kibit eastwood

lint-unused:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test eastwood \
	"{:linters [:unused-fn-args \
	            :unused-locals \
	            :unused-namespaces \
	            :unused-private-vars \
	            :wrong-ns-form] \
	  :namespaces [:source-paths]}"

lint-ns:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test eastwood \
	"{:linters [:unused-namespaces :wrong-ns-form] \
	  :namespaces [:source-paths]}"

kibit-all:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test,+examples kibit

eastwood-all:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test,+examples eastwood \
	"{:namespaces [:source-paths]}"

lint-all: kibit-all eastwood-all

lint-unused-all:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test,+examples eastwood \
	"{:linters [:unused-fn-args \
	            :unused-locals \
	            :unused-namespaces \
	            :unused-private-vars \
	            :wrong-ns-form] \
	  :namespaces [:source-paths]}"

lint-ns-all:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test,+examples eastwood \
	"{:linters [:unused-namespaces :wrong-ns-form] \
	  :namespaces [:source-paths]}"

clj17-tests:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj17,+test midje

clj18-tests:
	@OAUTH2_CLIENT_ID=$(OAUTH2_CLIENT_ID) \
	OAUTH2_CLIENT_SECRET=$(OAUTH2_CLIENT_SECRET) \
	OAUTH2_CALLBACK_URL=$(OAUTH2_CALLBACK_URL) \
	lein with-profile +clj18,+test midje

all-tests: clj17-tests clj18-tests

check: kibit clj18-tests

check-all: lint clj17-tests clj18-tests
