package com.waes.assessment.common;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;

public class JsonUtilTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Test
	public void givenTwoEqualPayloads_whenTheyAreDiffed_thenAnEmptyMapShouldBeReturned() throws IOException {
		// GIVEN two equal payloads
		final String payload = "{\"field\":\"value\"}";
		final JsonNode leftNode = OBJECT_MAPPER.readTree(payload);
		final JsonNode rightNode = OBJECT_MAPPER.readTree(payload);;
		
		// WHEN they are diffed
		final Map<String, Map<String, Object>> result = JsonUtil.buildDiffMapResult(leftNode, rightNode);
		
		// THEN an empty Map should be returned
		Assert.assertTrue("An empty map is expected for equals JsonNode", result.isEmpty());
	}

	@Test
	public void givenTwoCompletelyDifferentPayloads_whenTheyAreDiffed_thenAFilledMapShouldBeReturned() throws IOException {
		// GIVEN two completely different payloads
		final JsonNode leftNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\"}");
		final JsonNode rightNode = OBJECT_MAPPER.readTree("{\"field2\":\"value2\"}");;
		
		// WHEN they are diffed
		final Map<String, Map<String, Object>> result = JsonUtil.buildDiffMapResult(leftNode, rightNode);
		
		// THEN a filled Map should be returned
		Assert.assertFalse("A filled map is expected for completely different JsonNode", result.isEmpty());
		Assert.assertEquals("value1", result.get("field1").get("left"));
		Assert.assertEquals(Constant.UNAVAILABLE_FIELD, result.get("field1").get("right"));
		
		Assert.assertEquals(Constant.UNAVAILABLE_FIELD, result.get("field2").get("left"));
		Assert.assertEquals("value2", result.get("field2").get("right"));
	}
	
	@Test
	public void givenTwoDifferentPayloads_whenTheyAreDiffed_thenAFilledMapShouldBeReturned() throws IOException {
		// GIVEN two different payloads
		final JsonNode leftNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\",\"field2\":\"value3\",\"field3\":{\"innerField1\":\"innerValue1\"}}");
		final JsonNode rightNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\",\"field2\":\"value2\",\"field3\":{\"innerField1\":\"innerValue2\"}}");
		
		// WHEN they are diffed
		final Map<String, Map<String, Object>> result = JsonUtil.buildDiffMapResult(leftNode, rightNode);

		// THEN a filled Map should be returned
		Assert.assertFalse("A filled map is expected for different JsonNode", result.isEmpty());
		Assert.assertEquals("innerValue1", result.get("field3.innerField1").get("left"));
		Assert.assertEquals("innerValue2", result.get("field3.innerField1").get("right"));
		
		Assert.assertEquals("value3", result.get("field2").get("left"));
		Assert.assertEquals("value2", result.get("field2").get("right"));
	}
	
	@Test
	public void givenTwoDifferentPayloadsWithDifferentObjects_whenTheyAreDiffed_thenAFilledMapShouldBeReturned() throws IOException {
		// GIVEN two different payloads with different objects
		final JsonNode leftNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\",\"field2\":\"value3\",\"field3\":{\"innerField1\":\"innerValue1\"}}");
		final JsonNode rightNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\",\"field2\":\"value2\",\"field3\":true}");
		
		// WHEN they are diffed
		final Map<String, Map<String, Object>> result = JsonUtil.buildDiffMapResult(leftNode, rightNode);

		// THEN a filled Map should be returned
		Assert.assertFalse("A filled map is expected for different JsonNode", result.isEmpty());
		Assert.assertEquals("innerValue1", ((JsonNode) result.get("field3").get("left")).get("innerField1").asText());
		Assert.assertEquals(true, ((BooleanNode) result.get("field3").get("right")).asBoolean());
		
		Assert.assertEquals("value3", result.get("field2").get("left"));
		Assert.assertEquals("value2", result.get("field2").get("right"));
	}
	
	@Test
	public void givenTwoDifferentPayloadsWithDifferentObjectsOposite_whenTheyAreDiffed_thenAFilledMapShouldBeReturned() throws IOException {
		// GIVEN two different payloads with different objects oposite
		final JsonNode leftNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\",\"field2\":\"value3\",\"field3\":true}");
		final JsonNode rightNode = OBJECT_MAPPER.readTree("{\"field1\":\"value1\",\"field2\":\"value2\",\"field3\":{\"innerField1\":\"innerValue1\"}}");
		
		// WHEN they are diffed
		final Map<String, Map<String, Object>> result = JsonUtil.buildDiffMapResult(leftNode, rightNode);

		// THEN a filled Map should be returned
		Assert.assertFalse("A filled map is expected for different JsonNode", result.isEmpty());
		Assert.assertEquals("innerValue1", ((JsonNode) result.get("field3").get("right")).get("innerField1").asText());
		Assert.assertEquals(true, ((BooleanNode) result.get("field3").get("left")).asBoolean());
		
		Assert.assertEquals("value3", result.get("field2").get("left"));
		Assert.assertEquals("value2", result.get("field2").get("right"));
	}
}
