package com.machina.client;

import com.machina.research.ResearchTree;

public class ClientResearch {
	private static ResearchTree research;

	public static ResearchTree getResearch() {
		return research;
	}

	public static void setResearch(ResearchTree research) {
		ClientResearch.research = research;
	}
}
