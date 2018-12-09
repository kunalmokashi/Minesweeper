package minesweeper;

import java.util.HashMap;
import java.util.Map;

public class Permutations {

	private Map<Integer, String[]> twoMap = new HashMap<>();
	private Map<Integer, String[]> threeMap = new HashMap<>();
	private Map<Integer, String[]> fourMap = new HashMap<>();
	private Map<Integer, String[]> fiveMap = new HashMap<>();
	private Map<Integer, String[]> sixMap = new HashMap<>();
	private Map<Integer, String[]> sevenMap = new HashMap<>();
	private Map<Integer, String[]> eightMap = new HashMap<>();
	
	public Permutations() {
		buildMaps();
	}
	private void buildMaps() {
		twoMap.put(1, new String[] {"10", "01"});
		twoMap.put(2, new String[] {"11"});
		
		threeMap.put(1, new String[] {"100","001","010"});
		threeMap.put(2, new String[] {"110","011","101"});
		threeMap.put(3, new String[] {"111"});
		
		fourMap.put(1, new String[] {"1000","0100","0010","0001"});
		fourMap.put(2, new String[] {"1100","1010","1001","0011","0110","0101"});
		fourMap.put(3, new String[] {"1110","1101","0111","1011"});
		fourMap.put(4, new String[] {"1111"});
		
		fiveMap.put(1, new String[] {"10000","01000","00100","00010","00001"});
		fiveMap.put(2, new String[] {"11000","10100","10010","10001","01100","01010","01001","00110","00101","00011"});
		fiveMap.put(3, new String[] {"11100","11010","11001","10110","10101","10011","01110","01011","01101","00111"});
		fiveMap.put(4, new String[] {"11110","10111","11011","11101","01111"});
		fiveMap.put(5, new String[] {"11111"});
		
		sixMap.put(1, new String[] {"100000","010000","001000","000100","000010", "000001"});
		sixMap.put(2, new String[] {"110000","101000","100100","100010","100001","011000","010100","010001","001100","001010","001001","000101","000110","000011"});
		sixMap.put(3, new String[] {"111000","110100","110010","110001","101100","101010","101001","100110","100101","100011","011100","011010","011001","010110","010101","001110","001101","000111","010011","010110"});
		sixMap.put(4, new String[] {"111100","010111", "011011", "011101", "011110", "100111", "101011", "101101", "110011", "110101", "110110", "111010", "111001", "001111"});
		sixMap.put(5, new String[] {"111110","110111","111011","111101","011111","101111"});
		sixMap.put(6, new String[] {"111111"});
		
		sevenMap.put(1, new String[] {"1000000","0100000","0010000","0001000","0000100", "0000010", "0000001"});
		sevenMap.put(2, new String[] {"1100000","1010000","1001000","1000100","1000010","1000001", "0110000","0101000","0100100","0100010","0011000","0010100","0010010","0010001","0001100","0001010", "0001001","0000110","0000101","0000011"});
		sevenMap.put(3, new String[] {"1110000","1101000","1100100","1100010","1100001", "1011000","1010100","1010010","1001100","1001010","1000110","1010001","1001001","1000101","1000011", "0111000","0110100","0110010","0110001", "0101100","0101010","0100110","0101100","0011100","0011010","0011001","0010110","0010011","0001110","0001101","0001011","0000111"});
		sevenMap.put(4, new String[] {"0001111","0010111","0011011","0011101","0011110", "0100111","0101011","0101101","0110011","0110101","0111001","0101110","0110110","0111010","0111100", "1000111","1001011","1001101","1001110", "1010011","1010101","1011001","1010011","1100011","1100101","1100110","1101001","1101100","1110001","1110010","1110100","1111000"});
		sevenMap.put(5, new String[] {"0011111","0101111","0110111","0111011","0111101","0111110", "1001111","1010111","1011011","1011101","1100111","1101011","1101101","1101110","1110011","1110101", "1110110","1111001","1111010","1111100"});
		sevenMap.put(6, new String[] {"1111110","1101111","1110111","1111011","0111111","1011111", "1111101"});
		sevenMap.put(7, new String[] {"1111111"});
		
	}
	public Map<Integer, String[]> getTwoMap() {
		return twoMap;
	}
	public Map<Integer, String[]> getThreeMap() {
		return threeMap;
	}
	public Map<Integer, String[]> getFourMap() {
		return fourMap;
	}
	public Map<Integer, String[]> getFiveMap() {
		return fiveMap;
	}
	public Map<Integer, String[]> getSixMap() {
		return sixMap;
	}
	public Map<Integer, String[]> getSevenMap() {
		return sevenMap;
	}
	public Map<Integer, String[]> getEightMap() {
		return eightMap;
	}
}