package test.org.evan.libraries.rocketmq.support.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别
 */
public enum SexEnum {
	/** 男 */
	MAN(1, "男"),
	/** 女 */
	WOMAN(2, "女");

	private static Map<Integer, SexEnum> pool = new HashMap<Integer, SexEnum>();

	static {
		for (SexEnum each : SexEnum.values()) {
			SexEnum defined = pool.get(each.getValue());
			if (null != defined) {
				throw new IllegalArgumentException(defined.toString()
						+ " defined as same code with " + each.toString());
			}
			pool.put(each.getValue(), each);
		}
	}

	private int value;
	private String text;

	SexEnum(int value, String text) {
		this.value = value;
		this.text = text;

	}

	public static SexEnum valueOf(int value) {
		return pool.get(value);
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

}
