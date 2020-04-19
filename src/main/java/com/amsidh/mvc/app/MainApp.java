package com.amsidh.mvc.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MainApp {

	public static void main(String[] args) throws IOException {
		// String schemaPath = "/storage-schema.json";
		String schemaPath = "/schema/product-schema.json";
		String payloadPath = "/payload/product-payload.json";
		MainApp mainApp = new MainApp();
		mainApp.validate(schemaPath, payloadPath);

	}

	public void validate(String schemaPath, String payloadPath) throws IOException {
		JSONObject schemaJsonObject = new JSONObject(new JSONTokener(getInputStream(schemaPath)));
		final Schema schema = SchemaLoader.load(schemaJsonObject);

		// JSONObject payload = new JSONObject(new
		// JSONTokener(getInputStream(payloadPath)));
		String payloadString = IOUtils.toString(getInputStream(payloadPath), StandardCharsets.UTF_8.name());

		JSONArray jsonArray = new JSONArray(payloadString);

		jsonArray.forEach(payload -> {

			JSONObject jsonObject = (JSONObject) payload;
			try {
				schema.validate(jsonObject);
			} catch (ValidationException validationException) {
				if (validationException.getCausingExceptions().size() > 0) {
					validationException.getCausingExceptions().forEach(exception -> {
						ValidationException forEachValidationException = (ValidationException) exception;
						System.out.println(forEachValidationException.getMessage());
					});
				} else {
					System.out.println(validationException.getMessage());
				}

			}
		});

	}

	private static InputStream getInputStream(String filePath) {

		return MainApp.class.getResourceAsStream(filePath);
	}

}
