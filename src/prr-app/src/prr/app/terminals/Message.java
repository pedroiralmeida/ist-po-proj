package prr.app.terminals;

/**
 * Messages.
 */
interface Message {

	/**
	 * @param key int key
	 * @return string reporting a duplicate mobile keyentifier
	 */
	static String duplicateMobile(int key) {
		return "O número'" + key + "' já está atribuído.";
	}

	/**
	 * @return string representing a mobile on the on state.
	 */
	static String mobileOn() {
		return "LIGADO";
	}

	/**
	 * @return string representing a mobile on the silence state.
	 */
	static String mobileSilence() {
		return "SILENCIO";
	}

	/**
	 * @return string representing a mobile on the off state.
	 */
	static String mobileOff() {
		return "DESLIGADO";
	}

	/**
	 * @return string representing a 2G mobile.
	 */
	static String mobile2G() {
		return "2G";
	}

	/**
	 * @return string representing a 3G mobile.
	 */
	static String mobile3G() {
		return "3G";
	}

	/**
	 * @return string representing a telemovel.
	 */
	static String telemovel() {
		return "TELEMOVEL";
	}
}
