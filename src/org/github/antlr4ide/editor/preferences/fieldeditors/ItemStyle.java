package org.github.antlr4ide.editor.preferences.fieldeditors;

import org.eclipse.swt.graphics.RGB;

public class ItemStyle {
	private final RGB DEFAULT_FG_RGB=new RGB(0,0,0); // black
	private final RGB DEFAULT_BG_RGB=new RGB(255,255,255); // white
	
	private Boolean enabled;
	private RGB fg;
	private RGB bg;
	private Boolean bold;
	private Boolean italic;
	private Boolean underlined;
	private Boolean strikethru;
	// font ?
	
	/**
	 * Creates an ItemStyle with default values.
	 */
	public ItemStyle() {
		this(null);
	}
	
	/**
	 * Initialize from a style
	 * @param style
	 */
	public ItemStyle(ItemStyle style) {
		if(style==null) setDefaultItemStyle();
		else {
		setEnabled(style.isEnabled());
		setBold(style.isBold());
		setItalic(style.isItalic());
		setUnderlined(style.isUnderlined());
		setStrikethru(style.isStrikethru());
		setFg(style.getFg());
		setBg(style.getBg());
		}
	}

	/**
	 * Default style element
	 */
	private void setDefaultItemStyle() {
		setEnabled(false);
		setBold(false);
		setItalic(false);
		setUnderlined(false);
		setStrikethru(false);
		setFg(DEFAULT_FG_RGB);
		setBg(DEFAULT_BG_RGB);
	}
			
	
	public Boolean isEnabled() {return enabled;}
	public void setEnabled(Boolean enabled) {this.enabled = enabled;}
	public RGB getFg() {return fg;}
	public void setFg(RGB fg) {	this.fg = fg;}
	public void setFg(String strRGB) { this.fg = RGBfromString(strRGB,DEFAULT_FG_RGB);}
	public RGB getBg() {return bg;}
	public void setBg(RGB bg) {	this.bg = bg;}
	public void setBg(String strRGB) {	this.bg = RGBfromString(strRGB,DEFAULT_BG_RGB);}
	public Boolean isBold() {return bold;}
	public void setBold(Boolean bold) {	this.bold = bold;}
	public Boolean isItalic() {	return italic;}
	public void setItalic(Boolean italic) {	this.italic = italic;}
	public Boolean isUnderlined() {	return underlined;}
	public void setUnderlined(Boolean underlined) {	this.underlined = underlined;}
	public Boolean isStrikethru() {	return strikethru;}
	public void setStrikethru(Boolean strikethru) {	this.strikethru = strikethru;}

	private RGB RGBfromString(String strRGB, RGB defaultRgb) {
		// strRBG: "RGB {255, 255, 255}"
		try {
		String s[]=strRGB.substring(5, strRGB.length()-1).split(",");
		int r = Integer.parseInt(s[0].trim());
		int g = Integer.parseInt(s[1].trim());
		int b = Integer.parseInt(s[2].trim());
		
		return new RGB(r,g,b);
		}
		catch (IndexOutOfBoundsException e) { }
		catch (NumberFormatException e) { }
		
		return defaultRgb;
	}
}
