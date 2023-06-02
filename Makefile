build:
	.\mvnw compile quarkus:build

check:
	.\mvnw test

publish:
	helm upgrade --install quarkus-api ./my-api-chart
