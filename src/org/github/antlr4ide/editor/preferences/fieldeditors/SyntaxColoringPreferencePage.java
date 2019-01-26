package org.github.antlr4ide.editor.preferences.fieldeditors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


public class SyntaxColoringPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private static Map<Integer,String> hiliteElements ;  // default from ANTLRv4Scanner.java
	static {
		   hiliteElements = new LinkedHashMap<Integer, String>();
		   // Statements
		   hiliteElements.put(ANTLRv4Lexer.FRAGMENT, "Lexer Fragment");
		   hiliteElements.put(ANTLRv4Lexer.CHANNELS, "Lexer Channels");
		   hiliteElements.put(ANTLRv4Lexer.PARSER,   "Parser");
		   hiliteElements.put(ANTLRv4Lexer.LEXER,    "Lexer");
		   hiliteElements.put(ANTLRv4Lexer.OPTIONS,  "Options");
		   hiliteElements.put(ANTLRv4Lexer.TOKENS,   "Tokens");
		   hiliteElements.put(ANTLRv4Lexer.GRAMMAR,  "Grammar");
		   hiliteElements.put(ANTLRv4Lexer.IMPORT,   "Import");

		   // Constants
		   hiliteElements.put(ANTLRv4Lexer.INT,            "Integer");
		   hiliteElements.put(ANTLRv4Lexer.STRING_LITERAL, "Strings");
		   
		   // Comments
		   hiliteElements.put(ANTLRv4Lexer.DOC_COMMENT,   "Doc Comment");
		   hiliteElements.put(ANTLRv4Lexer.BLOCK_COMMENT, "Block Comment");
		   hiliteElements.put(ANTLRv4Lexer.LINE_COMMENT,  "Line Comment");
		   
		   // References
		   hiliteElements.put(ANTLRv4Lexer.TOKEN_REF, "Token");
		   hiliteElements.put(ANTLRv4Lexer.RULE_REF,  "Rule");

		}
	
//	private FontMetrics fFontMetrics;
	@Override
	public void init(IWorkbench workbench) {
		System.out.println("SyntaxColoringPreferencePage - init" );

		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Syntax Coloring");		
		
	}

	@Override
	protected Control createContents(Composite parent) {
		System.out.println("SyntaxColoringPreferencePage - createControl" );
		initializeDialogUnits(parent);
		return createSyntaxPage(parent);
	}
	
	// Lot of inspiration from the CDT project : https://git.eclipse.org/c/cdt/org.eclipse.cdt.git/tree/core/org.eclipse.cdt.ui/src/org/eclipse/cdt/internal/ui
	private Control createSyntaxPage(Composite parent) {
		System.out.println("SyntaxColoringPreferencePage - createSyntaxPage" );
		Composite colorComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		colorComposite.setLayout(layout);

		/*
		 * Header: Syntax Elements
		 */
		Label label;
		label = new Label(colorComposite, SWT.LEFT);
		label.setText("Syntax Elements");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* 
		 * Composite for List of elements and style selector/editor
		 */
		Composite editorComposite = new Composite(colorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		editorComposite.setLayoutData(gd);
		
		/*
		 * List of elements 
		 */
		org.eclipse.swt.widgets.List elementList=new org.eclipse.swt.widgets.List(editorComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		SelectionListener elementListListener= new ElementListListener();
		elementList.addSelectionListener(elementListListener);
		initElementList(elementList);
		//elementList.
		
		/*
		 * Style editor
		 */
		Composite stylesComposite = new Composite(editorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		CheckBoxEditor styleEnabled = new CheckBoxEditor("enabled","Enable",stylesComposite) ;
		
		ColorFieldEditor colorForegroundField = new ColorFieldEditor("enabledFgRgb","Foreground",stylesComposite) ;
		ColorFieldEditor colorBackgroundField = new ColorFieldEditor("enabledBgRgb","Background",stylesComposite) ;
		RGB rgbBG=colorBackgroundField.getColorSelector().getColorValue();
		colorBackgroundField.getColorSelector().setColorValue(rgbBG);

		CheckBoxEditor styleBoldEnabled = new CheckBoxEditor("enabledBold","Bold",stylesComposite) ;
		CheckBoxEditor styleItalicEnabled = new CheckBoxEditor("enabledItalic","Italic",stylesComposite) ;
		CheckBoxEditor styleUnderlineEnabled = new CheckBoxEditor("enabledUnderline","Underline",stylesComposite) ;
		CheckBoxEditor styleStrikethruEnabled = new CheckBoxEditor("enabledStrikethru","Strikethrough",stylesComposite) ;

		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = convertWidthInCharsToPixels(20);
		gd.heightHint = convertHeightInCharsToPixels(5);
		gd.horizontalSpan = 2;
		
		
		label = new Label(editorComposite, SWT.LEFT);
		label.setText("Preview");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		

		//http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SettingthefontstyleforegroundandbackgroundcolorsofStyledText.htm
		StyledText text = new StyledText(editorComposite,SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setText("TEST PROGRAM"+Text.DELIMITER
				+ "This could be many lines of code"+Text.DELIMITER
				+ ""+Text.DELIMITER
				+ "Even here;"+Text.DELIMITER);
		text.append("TEST 2"+Text.DELIMITER);

		// public void setStyleRanges(int start, int length, int[] ranges, StyleRange[] styles)
		// int[] ranges: ranges[n]: start, ranges[n+1]: length, styles[n/2]: style
	    StyleRange style1 = new StyleRange(); //StyleRange(int start, int length, Color foreground, Color background, int fontStyle)
	    style1.start = 0;
	    style1.length = 10;
	    style1.fontStyle = SWT.BOLD;
	    text.setStyleRange(style1);

		
//	    TextStyle style1 = new TextStyle(font1, yellow, blue);
//	    TextStyle style2 = new TextStyle(font2, green, null);
//	    TextStyle style3 = new TextStyle(font3, blue, gray);
//
//	    layout.setText("English 1234567890asdfasdfasdf");
//	    layout.setStyle(style1, 0, 6);
//	    layout.setStyle(style2, 8, 10);
//	    layout.setStyle(style3, 13, 21);
	    
		return colorComposite;
	}
	
	
	
	/*
	 * When an list item is selected shows associated style attributes
	 */
	private class ElementListListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// invoked when double clicking on an item
			// TODO: set selected color and close widget
			System.out.println("SyntaxColoringPreferencePage.ElementListListner - widgetDefaultSelected "+e );
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			List list=(List) e.getSource();
			String selected[]=list.getSelection();
			for(String item:selected)
			System.out.println("SyntaxColoringPreferencePage.ElementListListner - widgetSelected " 
		    + " selection "+item
		);
		}
		
	}
	
	private class ItemStyle {
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
		public RGB getBg() {return bg;}
		public void setBg(RGB bg) {	this.bg = bg;}
		public Boolean isBold() {return bold;}
		public void setBold(Boolean bold) {	this.bold = bold;}
		public Boolean isItalic() {	return italic;}
		public void setItalic(Boolean italic) {	this.italic = italic;}
		public Boolean isUnderlined() {	return underlined;}
		public void setUnderlined(Boolean underlined) {	this.underlined = underlined;}
		public Boolean isStrikethru() {	return strikethru;}
		public void setStrikethru(Boolean strikethru) {	this.strikethru = strikethru;}
		
		public java.util.List<String> toPropertyString() {
			java.util.List<String> out=new ArrayList<String>();
			
			out.add(".styleEnabled="+isEnabled());
			out.add(".styleBold="+isBold());
			out.add(".styleItalic="+isItalic());
			out.add(".styleUnderlined="+isUnderlined());
			out.add(".styleStrikethru="+isStrikethru());
			out.add(".styleFgBlue="+getFg().blue);
			out.add(".styleFgGreen="+getFg().green);
			out.add(".styleFgRed="+getFg().red);
			out.add(".styleBgBlue="+getBg().blue);
			out.add(".styleBgGreen="+getBg().green);
			out.add(".styleBgRed="+getBg().red);

			return out;
		}
	}
	
	private void updateColorStyle(String itemName) {
		
	}
	
	private void initElementList(List elementList) {
		System.out.println("SyntaxColoringPreferencePage - initElementList" );
		Map<Integer,ItemStyle>hiliteItemStyle=new HashMap<>();
		
		for(Integer ix: hiliteElements.keySet()) {
			elementList.add(hiliteElements.get(ix));
			// set initial map of styles for each elements
			hiliteItemStyle.put(ix,getItemStyleProperty(ix));
		}
	}

	private ItemStyle getItemStyleProperty(Integer ix) {
		// TODO Auto-generated method stub
		IPreferenceStore ps = getPreferenceStore();
		ItemStyle is=new ItemStyle(); // default Item Style
		
		is.setEnabled(ps.getBoolean(pn(ix,"styleEnabled")));
		is.setBold(ps.getBoolean(pn(ix,"styleBold")));
		is.setItalic(ps.getBoolean(pn(ix,"styleItalic")));
		is.setUnderlined(ps.getBoolean(pn(ix,"styleUnderline")));
		is.setStrikethru(ps.getBoolean(pn(ix,"styleStrikethru")));
		if (ps.contains(pn(ix,"styleFgRed"))) {
			is.setFg(new RGB(
			 ps.getInt(pn(ix,"styleFgRed"))
			,ps.getInt(pn(ix,"styleFgBlue"))
			,ps.getInt(pn(ix,"styleFgGreen"))
			));
		}
		if (ps.contains(pn(ix,"styleBgRed"))) {
			is.setBg(new RGB(
			 ps.getInt(pn(ix,"styleBgRed"))
			,ps.getInt(pn(ix,"styleBgBlue"))
			,ps.getInt(pn(ix,"styleBgGreen"))
			));
		}
		
		return is;
	}

	/** 
	 * return property name for a style attribute
	 * 
	 * @param string
	 * @return
	 */
	private String pn(Integer ix, String styleAttribute) {
		// TODO Auto-generated method stub
		return "antlride.preference.syntax."+ix+"."+styleAttribute;
	}
}
