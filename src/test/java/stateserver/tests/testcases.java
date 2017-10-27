package stateserver.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.solutions.stateserver.Solution;

class testcases {


	private Solution solution = new Solution();
	private double longitude;
	private double latitude;
	// Given Test Case
	@Test
	void giventest() throws ParseException, IOException {
		longitude = -77.036133;
		latitude = 40.513799;
		
		solution.setLatitude(latitude);
		solution.setLongitude(longitude);
		
		assertEquals(Arrays.asList("Pennsylvania"), solution.getState());
	}
	
}
