package org.github.antlr4ide.editor.preferences.fieldeditors;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class AntlrVersionListEditor extends ListEditor {
		private String del=",";
		private Composite parent;

		public AntlrVersionListEditor(String label, String content, Composite parent) {
			super(label,content,parent);
			this.parent=parent;
		}

		@Override
		protected String createList(String[] items) {
			StringBuffer out=new StringBuffer();
			int m=items.length;
			for(int i=0; i<m; i++) { 
				out.append(items[i]);
				if(i+1<m) out.append(del);
			}
			return out.toString();
		}

		@Override
		protected String getNewInputObject() {
			FileDialog fd=new FileDialog(this.parent.getShell(),SWT.OPEN);
			
			String [] filterNames = new String [] {"JAR Files"};
			String [] filterExtensions = new String [] {"*.jar"};
			fd.setFilterExtensions(filterExtensions);
			fd.setFilterNames(filterNames);
			fd.setFileName ("antlr*.jar");
			fd.setText("Select Antlr tool jar");
			
			String toolJarName=fd.open();
			if(toolJarName==null) return null; // Error or cancel in file dialog.

			StringBuffer out=new StringBuffer();
			boolean isOk=verifyTooljar(toolJarName,out);
			if(isOk) return out.toString();
			
			return null;
		}

		@Override
		protected String[] parseString(String stringList) {
			return stringList.split(del);
		}
		
		// Keep format in sync with AntlrToolConfig.getToolJar
		private boolean verifyTooljar(String jarfilename, StringBuffer out)  {
			File f=new File(jarfilename);
			String TOOL = "org.antlr.v4.Tool"; // TODO move to AntlrPreferenceConstants
		
			try {
				JarInputStream jarjar = new JarInputStream(new FileInputStream(f));
				Attributes attributes=jarjar.getManifest().getMainAttributes(); 
				
				String version = (String) attributes.get(new Attributes.Name("Implementation-Version"));
				String mainClass = (String) attributes.get(new Attributes.Name("Main-Class"));
				jarjar.close();

				if(mainClass.equals(TOOL)) {
				   out.append(version);
 				   out.append(" ");
				   out.append(f.getAbsolutePath());
				   
				   // enable P_TOOL_STORE_PARSETREE field
				   // TODO: Fix sequence: verifyTooljar invoked when distribution is selected, but this can happen before the field is created.
				   // fieldStoreParseTree.setEnabled(verifyTooljarSerializable(f), getFieldEditorParent());
				   
 				   return true;
				}
			} catch (Exception e) {
				System.out.println("Exception caught, but otherwise ignored.");
				e.printStackTrace();
			}
			
			return false;
		}
		
		private boolean verifyTooljarSerializable (File f) {
			// http://www.javassist.org/html/index.html
			// https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
//			JarFile jarFile = new JarFile(pathToJar);
//			Enumeration<JarEntry> e = jarFile.entries();
//
//			URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
//			URLClassLoader cl = URLClassLoader.newInstance(urls);
//
//			while (e.hasMoreElements()) {
//			    JarEntry je = e.nextElement();
//			    if(je.isDirectory() || !je.getName().endsWith(".class")){
//			        continue;
//			    }
//			    // -6 because of .class
//			    String className = je.getName().substring(0,je.getName().length()-6);
//			    className = className.replace('/', '.');
//			    Class c = cl.loadClass(className);
//
//			}
			
			
			// http://www.java2s.com/Tutorial/Java/0125__Reflection/ReturnstrueifaclassimplementsSerializableandfalseotherwise.htm
			// check if class implements Serializable  return (Serializable.class.isAssignableFrom(c));
			
			// Check if  
			//    org.antlr.v4.runtime.RuleContext.class
			// implements Serializable
			
			return true;
		}
	}
