package com.exacs.ecra.errors;

public enum ErrorCodes {

	FIELD_VALUE_SHOULD_BE_PROVIDED("FIELD_VALUE_SHOULD_BE_PROVIDED", "field.value.should.be.provided", null, null),
	RACK_ID_INVALID("RACK_ID_INVALID","rack.id.invalid",null,null),
	COMPUTE_NODES_REQUEST_NOT_PRESENT("COMPUTE_NODES_REQUEST_NOT_PRESENT", "compute.nodes.request.not.present", null, null);

	private final String code;
	private final String messageKey;
	private final String detailKey;
	private final String moreInfoKey;

	ErrorCodes(final String code, final String messageKey, final String detailKey, final String moreInfoKey) {
		this.code = code;
		this.messageKey = messageKey;
		this.detailKey = detailKey;
		this.moreInfoKey = moreInfoKey;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getCode() {
		return code;
	}

	public String getDetailKey() {
		return detailKey;
	}

	public String getMoreInfoKey() {
		return moreInfoKey;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("code: ").append(code);
		builder.append(", messageKey: ").append(messageKey);
		builder.append(", detailKey: ").append(detailKey);
		builder.append(", moreInfoKey: ").append(moreInfoKey);
		return builder.toString();
	}
}
