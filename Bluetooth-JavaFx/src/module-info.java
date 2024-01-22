module javafx_btt {
	 requires transitive javafx.controls;
	 requires javafx.fxml;
	 requires transitive javafx.graphics;
	 requires bluecove;
	 
	 opens org.btt_javafx to javafx.fxml,javafx.graphics,bluecove;
	 
	 exports org.btt_javafx;
	 
}