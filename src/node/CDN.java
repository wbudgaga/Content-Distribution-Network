package node;

//This interface contains of the constants that are used in this project
public interface CDN {
	public 	static	final	String	MST_ALGORITHM 	= "MSTKruskal";
	public 	static 	final 	String	DISCOVERYID 	= "discovery";	
	public	static 	final 	int 	CR				= 4; // the number of connections per Router in CDN
	public	static 	final 	int 	MIN_WEIGHT		= 1;
	public 	static 	final 	int 	MAX_WEIGHT		= 10;
	public 	static 	final 	int 	BUFFERSIZE		= 10240;
	public 	static 	final 	long 	REFRESH_INTERVAL= 120000;	
}
