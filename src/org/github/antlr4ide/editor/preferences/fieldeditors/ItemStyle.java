package org.github.antlr4ide.editor.preferences.fieldeditors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * The ItemStyle is designed to be used with the SyntaxColoringPreferencePage
 * for the style of the individual syntax elements.
 * 
 * 
 */
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
		setDefaultItemStyle();
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

	public ItemStyle(TextAttribute testAttribute) {
		this();
		if(testAttribute==null) return;
		
		if(testAttribute.getForeground()!=null)
		  setFg(testAttribute.getForeground().getRGB());
		
		if(testAttribute.getBackground()!=null)
		  setBg(testAttribute.getBackground().getRGB());
		
		int textStyle=testAttribute.getStyle();
		
		setEnabled(true);
		if((textStyle&SWT.BOLD)!=0)  setBold(true);
		if((textStyle&SWT.ITALIC)!=0) setItalic(true);
		if((textStyle&TextAttribute.UNDERLINE)!=0) setUnderlined(true);
		if((textStyle&TextAttribute.STRIKETHROUGH)!=0) setStrikethru(true);
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
	/**
	 * Set Foreground Color from a RGB string
	 * @param strRGB "RGB {255, 255, 255}"
	 */
	public void setFg(String strRGB) { this.fg = parseRGBString(strRGB,DEFAULT_FG_RGB);}
	public RGB getBg() {return bg;}
	public void setBg(RGB bg) {	this.bg = bg;}
	/**
	 * Set Background Color from a RGB string
	 * @param strRGB "RGB {255, 255, 255}"
	 */
	public void setBg(String strRGB) {	this.bg = parseRGBString(strRGB,DEFAULT_BG_RGB);}
	public Boolean isBold() {return bold;}
	public void setBold(Boolean bold) {	this.bold = bold;}
	public Boolean isItalic() {	return italic;}
	public void setItalic(Boolean italic) {	this.italic = italic;}
	public Boolean isUnderlined() {	return underlined;}
	public void setUnderlined(Boolean underlined) {	this.underlined = underlined;}
	public Boolean isStrikethru() {	return strikethru;}
	public void setStrikethru(Boolean strikethru) {	this.strikethru = strikethru;}

	/**
	 * Return the font style. 
	 * Intended to be used for {@code StyleRange.fontStyle=itemStyle.asFontStyle(); }.
	 * @return bit masked integer with the set attributes
	 */
	public int toFontStyle() {
		int out=SWT.NORMAL;
		if(isBold()) out|=SWT.BOLD;
		if(isItalic()) out|=SWT.ITALIC;
		if(isUnderlined()) out|=TextAttribute.UNDERLINE;
		if(isStrikethru()) out|=TextAttribute.STRIKETHROUGH;

		return out;
	}
	
	public StyleRange toStyleRange(int start, int stop) {
//		System.out.println("SyntaxColoringPreferencePage.ElementListListner - applyPreviewTextItemStyle "+itemStyle+" "+start + " " + stop); 
		// public void setStyleRanges(int start, int length, int[] ranges, StyleRange[] styles)
		// int[] ranges: ranges[n]: start, ranges[n+1]: length, styles[n/2]: style
	    StyleRange styleRange = new StyleRange(); //StyleRange(int start, int length, Color foreground, Color background, int fontStyle)
	    styleRange.start = start;
	    styleRange.length = stop-start+1;
	    styleRange.fontStyle = toFontStyle();
	    styleRange.foreground = new Color(Display.getCurrent(),getFg());
	    styleRange.background = new Color(Display.getCurrent(),getBg());
	    return styleRange;
	}
	
	private RGB parseRGBString(String strRGB, RGB defaultRgb) {
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
