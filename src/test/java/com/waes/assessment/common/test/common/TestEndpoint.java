package com.waes.assessment.common.test.common;

/**
 * This enumerator is used only on the tests to help
 * to define the available endpoints to be called on the
 * integration test
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public enum TestEndpoint {

	V1_DIFF("/v1/diff/%s"),
	V1_LEFT("/v1/diff/%s/left"),
	V1_RIGHT("/v1/diff/%s/right");
	
	private String url;

	private TestEndpoint(final String url) {
		this.url = url;
	}

	/**
	 * Returns the parsed url related with the endpoint
	 * with the specific id
	 * 
	 * @param id to be requested
	 * @return {@link String} containing the parsed endpoint
	 */
	public String getUrl(final int id) {
		return String.format(this.url, id);
	}
}
