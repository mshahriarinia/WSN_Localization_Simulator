package simulator.localizable;

public class State {
	public enum LocalizationPhase {
		ANCHORS_RECEPTION, SEND_INITIAL_POSE, MASS_SPRING_REFINING, ANGULAR_REFINING, CLUSTERING
	}

	public static String toString(LocalizationPhase localizationPhase) {
		switch (localizationPhase) {
		case ANCHORS_RECEPTION:
			return "ANCHOR_RECEIPT";
		case SEND_INITIAL_POSE:
			return "SEND_INITIAL";
		case MASS_SPRING_REFINING:
			return "MS_REFINING";
		case ANGULAR_REFINING:
			return "ANGLE_REFINING";
		case CLUSTERING:
			return "CLUSTERING";
		}
		return "Invalid localization phase.";
	}
}
