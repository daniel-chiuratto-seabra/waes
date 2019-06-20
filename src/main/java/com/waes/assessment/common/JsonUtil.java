package com.waes.assessment.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * This utilitary class contains the method used to generate the {@link Map} which carries
 * the diff result from two {@link JsonNode}, used on the application to build
 * the analysis result of the two {@link JsonNode} set on both left and
 * right memories when they has the same size but different values
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public abstract class JsonUtil {

	/**
	 * This method processes both {@link JsonNode} set as parameter, to find which fields has difference
	 * and the difference itself
	 * 
	 * Its structure is built considering that we have a {@link Map} which has the field names as key,
	 * where those field names is concatenated using dot signal to separate, when we have a field
	 * which is contained inside of nested objects.
	 * 
	 * As the value of this structe we have another {@link Map} which handles the left and right keys,
	 * associating which memory each value shown in the result is related with.
	 * 
	 * Example:
	 * <br><br>
	 * <pre>
	 * LEFT MEMORY HAVING:                     RIGHT MEMORY HAVING:
	 * {                                       {
	 *    "field1": "value1",                     "field2": "value2",
	 *    "field2": "value2"                      "field3": "value3"
	 * }                                       }
	 * </pre>
	 * 
	 * The result of the diff above would be:
	 * <pre>
	 * {
	 *    "field1": {
	 *       "left": "value1",
	 *       "right": "unavailable field"
	 *    },
	 *    "field3": {
	 *       "left": "unavailable field",
	 *       "right": "value3"
	 *    }
	 * }
	 * </pre>
	 * 
	 * So the diff returns only the differences as shown in the example above.
	 * 
	 * @param leftNode {@link JsonNode} containing the payload coming from the left memory
	 * @param rightNode {@link JsonNode} containing the payload coming from the right memory
	 * 
	 * @return {@link Map} containing the diff result found on both {@link JsonNode} received by the parameter
	 */
	public static Map<String, Map<String, Object>> buildDiffMapResult(final JsonNode leftNode, final JsonNode rightNode) {

		final Map<String, Map<String, Object>> result = new HashMap<>();
		final StringBuilder sb = new StringBuilder();
		
		final Collection<String> fieldNames = concatenateFieldNames(leftNode.fieldNames());
		fieldNames.addAll(concatenateFieldNames(rightNode.fieldNames()));
		
		recursiveProcess(sb, result, leftNode, rightNode, fieldNames);
		
		return result;
	}

	/*
	 * This method was created to attend the approach of using recursion during the JsonNode analysis to build
	 * the diff Map result
	 */
	private static void recursiveProcess(final StringBuilder sb, final Map<String, Map<String, Object>> result, final JsonNode leftNode, final JsonNode rightNode, final Collection<String> fieldNames) {
		// First call an iteration because each JsonNode contains a set of fields that is dynamic, so
		// the best approach to avoid the need of knowing their names is iterating through them
		for (final String fieldName : fieldNames) {
			
			// After confirming that both JsonNode contains the respective field name in it
			// then we can proceed with their check
			if (leftNode.has(fieldName) && rightNode.has(fieldName)) {
				final JsonNode left = leftNode.get(fieldName);
				final JsonNode right = rightNode.get(fieldName);
				
				// Below in case of both JsonNode has difference, so we have three scenarios to check:
				// If both are Containers so we call this recursiveProcess method using its childs as parameters
				// If both are Values so we set their differences
				// Otherwise we consider that both are different being one Container and the other a Value
				// or vice-versa
				if (!left.equals(right))
					if (isBothContainerNode(left, right))
						processBothContainerNode(sb, result, fieldName, left, right);
					else if (isBothValueNode(left, right))
						processBothValueNode(sb, result, fieldName, left, right);
					else processDifferentNode(sb, result, fieldName, left, right);
 			} else {
 				// This block is accessed when one of the JsonNode does not contain the respective field name
 				// so here we find who is unavailable and in which of sides left or right
				appendPathDot(sb, fieldName);
 				
				// Here using ternary statements we define who has value and who's unavailable
 				final Object leftResult = leftNode.has(fieldName) ? leftNode.get(fieldName).asText() : Constant.UNAVAILABLE_FIELD;
 				final Object rightResult = rightNode.has(fieldName) ? rightNode.get(fieldName).asText() : Constant.UNAVAILABLE_FIELD;
 				
 				// We set the result of the previous process in a sub result to be added on the main result carrier
 				final Map<String, Object> subResult = new HashMap<>();
 				subResult.put(Endpoint.LEFT.getValue(), leftResult);
 				subResult.put(Endpoint.RIGHT.getValue(), rightResult);
 				
 				result.put(sb.toString(), subResult);
 				reducePath(sb);
 			}
		}
	}

	/*
	 * This method is used to process in the scenario when the iteration through the child nodes finds two nodes with different
	 * types like a Container and a Value node
	 */
	private static void processDifferentNode(final StringBuilder sb, final Map<String, Map<String, Object>> result,
			final String fieldName, final JsonNode left, final JsonNode right) {
		appendPathDot(sb, fieldName);
		
		final Map<String, Object> subResult = new HashMap<>();
		subResult.put(Endpoint.LEFT.getValue(), left);
		subResult.put(Endpoint.RIGHT.getValue(), right);
		
		result.put(sb.toString(), subResult);
		reducePath(sb);
	}

	/*
	 * This method is used to process the scenario when the iteration through the child nodes of the parent node finds
	 * two Value nodes to be set on the result
	 */
	private static void processBothValueNode(final StringBuilder sb, final Map<String, Map<String, Object>> result,
			final String fieldName, final JsonNode left, final JsonNode right) {
		appendPathDot(sb, fieldName);
		
		final Map<String, Object> subResult = new HashMap<>();
		subResult.put(Endpoint.LEFT.getValue(), left.asText());
		subResult.put(Endpoint.RIGHT.getValue(), right.asText());
		
		result.put(sb.toString(), subResult);
		reducePath(sb);
	}

	/*
	 * This method is used to process the scenario when the itetarion through the child nodes of the parent node finds
	 * two Container nodes to be processed recursively by the recursiveProcess method
	 */
	private static void processBothContainerNode(final StringBuilder sb, final Map<String, Map<String, Object>> result,
			final String fieldName, final JsonNode left, final JsonNode right) {
		appendPathDot(sb, fieldName);
		
		final Collection<String> fieldNames = concatenateFieldNames(left.fieldNames());
		fieldNames.addAll(concatenateFieldNames(right.fieldNames()));
		
		recursiveProcess(sb, result, left, right, fieldNames);
		reducePath(sb);
	}

	/*
	 * This method is used to check if both left and right nodes are Value nodes or not 
	 */
	private static boolean isBothValueNode(final JsonNode left, final JsonNode right) {
		return ValueNode.class.isAssignableFrom(left.getClass()) &&
				   ValueNode.class.isAssignableFrom(right.getClass());
	}

	/*
	 * This method is used to check if both left and right nodes are Container nodes or not
	 */
	private static boolean isBothContainerNode(final JsonNode left, final JsonNode right) {
		return ContainerNode.class.isAssignableFrom(left.getClass()) &&
			ContainerNode.class.isAssignableFrom(right.getClass());
	}

	/*
	 * This method is used to remove one part of the field path
	 * 
	 * Example:
	 * 	-> Previous: path1.path2.path3
	 *	-> After 1 Execution: path1.path2 
	 */
	private static void reducePath(final StringBuilder sb) {
		if (sb.toString().contains(".")) {
			final String aux = sb.toString().substring(0, sb.toString().lastIndexOf('.'));
			sb.setLength(0);
			sb.append(aux);
		} else sb.setLength(0);
	}
	
	/*
	 * This method does the opposite of the reducePath do, appending a field name on the path
	 * using the dot notation in it
	 */
	private static void appendPathDot(final StringBuilder sb, final String fieldName) {
		if (sb.length() > 0)
			sb.append(".");
		sb.append(fieldName);
	}
	
	private static Collection<String> concatenateFieldNames(final Iterator<String> lIter) {
		final Collection<String> fieldNames = new ArrayList<>();
		while (lIter.hasNext())
			fieldNames.add(lIter.next());
		return fieldNames;
	}
}
