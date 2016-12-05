build: clean
	@lein compile
	@lein uberjar

repl:
	@lein with-profile +clj18,+examples repl

clean-all: clean clean-docs

clean:
	@rm -rf target
	@rm -f pom.xml

mvn-tree:
	@lein pom
	@mvn dependency:tree

deps-tree:
	@lein with-profile +clj18,+examples,+test deps :tree
	@lein with-profile +clj18,+examples,+test deps :plugin-tree

loc:
	@find src -name "*.clj" -exec cat {} \;|wc -l
