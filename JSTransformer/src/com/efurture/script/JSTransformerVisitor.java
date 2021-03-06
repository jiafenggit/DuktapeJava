package com.efurture.script;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.*;

public class JSTransformerVisitor implements NodeVisitor {
	
	
	public static final String  PREFIX = "__";


	public static final String WEBPACK_PREFIX = "__webpack_require__";


	public boolean isLeftOk(PropertyGet  get){
		boolean leftOk = true;
		if(get.getLeft() instanceof  Name){
			Name left = (Name) get.getLeft();
			if(left.getIdentifier().contains(WEBPACK_PREFIX)){
				leftOk = false;
			}
		}
		return leftOk;
	}

    @Override 
    public boolean visit(AstNode node) {
      if (node.getParent() != null) {
    		JSTransformer.log("visit xxx " + node.getClass()  + "  " + node.toSource()
   				    + " " + node.getParent().getClass()); 
	 }
     if (node instanceof NewExpression) {
    	 NewExpression expression = (NewExpression) node;

    	 JSTransformer.log("new xxx " + expression.toSource());
    	 JSTransformer.log("new xxx " + expression.getInitializer() + "  "   +
    			 expression.getTarget().toSource());
    	 List<AstNode> arguments = expression.getArguments();
    	 for(int i=0; i< arguments.size(); i++){
    		 AstNode args = arguments.get(i);
    		 args.visit(this);
    	 }
		 return false;
	 }
  	  if(node instanceof FunctionCall && !(node instanceof NewExpression)){
  		   FunctionCall functionCall = (FunctionCall) node;
  		   AstNode tatget =  functionCall.getTarget();
  		   if (tatget instanceof PropertyGet) {
  			   PropertyGet  get = (PropertyGet)tatget;
  			   Name right = (Name) get.getRight();
  			   if (right != null && !right.getIdentifier().contains(PREFIX) && isLeftOk(get)) {
  				   StringLiteral literal = new StringLiteral();
  				   literal.setValue(right.getIdentifier());
  				   literal.setQuoteCharacter('"');
  				   ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  				   arguments.add(literal);
  				   arguments.addAll(functionCall.getArguments());
  				   functionCall.setArguments(arguments);
  				   right.setIdentifier("__c");
				   }
  		   }else{
  			   //System.out.println("function call " + functionCall.toSource() + "  "  + tatget.getClass());
  		   }
  	 }else if (node instanceof PropertyGet) {
  		     PropertyGet  get = (PropertyGet) node;
		       Name right = (Name) get.getRight();

  		      if (!right.getIdentifier().contains(PREFIX) && isLeftOk(get)) {
  	  		     if (get.getParent() instanceof  ExpressionStatement) {
  	  			      ExpressionStatement parent = (ExpressionStatement) get.getParent();
  		    		  FunctionCall call = new FunctionCall();
  		    		  StringLiteral literal = new StringLiteral();
  					  literal.setValue(right.getIdentifier());
  					  literal.setQuoteCharacter('"');
  					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  					  arguments.add(literal);
  		    		  call.setArguments(arguments);
  		    		  call.setTarget(get);
  		    		  right.setIdentifier("__g");
  		    		  parent.setExpression(call);
  				 }else if(get.getParent() instanceof  PropertyGet){
  					 PropertyGet parent = (PropertyGet) get.getParent();
  					  FunctionCall call = new FunctionCall();
  		    		  StringLiteral literal = new StringLiteral();
  					  literal.setValue(right.getIdentifier());
  					  literal.setQuoteCharacter('"');
  					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  					  arguments.add(literal);
  		    		  call.setArguments(arguments);
  		    		  call.setTarget(get);
  		    		  right.setIdentifier("__g");
  		    		  parent.setLeft(call);
  					 // System.out.println("get xxx " + node.getClass()  + "  " + node.toSource());
  				 }else if(get.getParent() instanceof  InfixExpression){
  					  InfixExpression  parent = (InfixExpression) get.getParent();
  		    		  FunctionCall call = new FunctionCall();
  		    		  StringLiteral literal = new StringLiteral();
  					  literal.setValue(right.getIdentifier());
  					  literal.setQuoteCharacter('"');
  					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  					  arguments.add(literal);
  		    		  call.setArguments(arguments);
  		    		  call.setTarget(get);
  		    		  right.setIdentifier("__g");
  		    		  if (parent.getLeft() == get) {
  		    			   parent.setLeft(call);
  					  }else {
  						  parent.setRight(call);
  					  }
  				 }else if(get.getParent() instanceof  FunctionCall){
  					  FunctionCall  parent = (FunctionCall) get.getParent();
  		    		  FunctionCall call = new FunctionCall();
  		    		  StringLiteral literal = new StringLiteral();
  					  literal.setValue(right.getIdentifier());
  					  literal.setQuoteCharacter('"');
  					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  					  arguments.add(literal);
  		    		  call.setArguments(arguments);
  		    		  call.setTarget(get);
  		    		  right.setIdentifier("__g");
  		    		  ArrayList<AstNode>  parentArguments = new ArrayList<AstNode>();
  		    		  parentArguments.addAll(parent.getArguments());
  		    		  int index = parentArguments.indexOf(get);
  		    		  parentArguments.set(index, call);
  		    		  parent.setArguments(parentArguments);
  				 }else if(get.getParent() instanceof  VariableInitializer){
  					  VariableInitializer  parent = (VariableInitializer) get.getParent();
  		    		  FunctionCall call = new FunctionCall();
  		    		  StringLiteral literal = new StringLiteral();
  					  literal.setValue(right.getIdentifier());
  					  literal.setQuoteCharacter('"');
  					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  					  arguments.add(literal);
  		    		  call.setArguments(arguments);
  		    		  call.setTarget(get);
  		    		  right.setIdentifier("__g");
  		    		  parent.setInitializer(call);
  				 }else if(get.getParent() instanceof ElementGet){
  					 ElementGet  parent = (ElementGet) get.getParent();
  		    		  FunctionCall call = new FunctionCall();
  		    		  StringLiteral literal = new StringLiteral();
  					  literal.setValue(right.getIdentifier());
  					  literal.setQuoteCharacter('"');
  					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
  					  arguments.add(literal);
  		    		  call.setArguments(arguments);
  		    		  call.setTarget(get);
  		    		  right.setIdentifier("__g");
  		    		  parent.setElement(call);
  				 }else{

  					JSTransformer.log("get xxx " + node.getClass()  + "  " + node.toSource()
  					    + " " + node.getParent().getClass());
  				 }
			 }else{
				 JSTransformer.log("get out " + node.getClass()  + "  " + node.toSource()
	  					    + " " + node.getParent().getClass());
			 }
		 }else if(node instanceof ElementGet){

			 ElementGet get = (ElementGet) node;
			 if (node.getParent() instanceof NewExpression) {
				 NewExpression parent = (NewExpression) get.getParent();
				 FunctionCall call = new FunctionCall();
		    	 ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
			     arguments.add(get.getElement());
		    	 call.setArguments(arguments);
		    	 PropertyGet target = new PropertyGet();
		    	 Name name = new Name();
		    	 name.setIdentifier("__g");
		    	 target.setLeft(get.getTarget());
		    	 target.setRight(name);
		    	 call.setTarget(target);
		    	 ArrayList<AstNode>  parentArguments = new ArrayList<AstNode>();
		    	 parentArguments.addAll(parent.getArguments());
		    	 int index = parentArguments.indexOf(get);
		    	 parentArguments.set(index, call);
		    	 parent.setArguments(parentArguments);
			}else if (node.getParent() instanceof  ExpressionStatement){
				 FunctionCall call = new FunctionCall();
				 ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
				 arguments.add(get.getElement());
				 call.setArguments(arguments);
				 PropertyGet target = new PropertyGet();
				 Name name = new Name();
				 name.setIdentifier("__g");
				 target.setLeft(get.getTarget());
				 target.setRight(name);
				 call.setTarget(target);
				 ExpressionStatement parent = (ExpressionStatement) node.getParent();
				 parent.setExpression(call);
			 }else if (node.getParent() instanceof  VariableInitializer){
				 FunctionCall call = new FunctionCall();
				 ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
				 arguments.add(get.getElement());
				 call.setArguments(arguments);
				 PropertyGet target = new PropertyGet();
				 Name name = new Name();
				 name.setIdentifier("__g");
				 target.setLeft(get.getTarget());
				 target.setRight(name);
				 call.setTarget(target);

				 VariableInitializer parent = (VariableInitializer) node.getParent();
				 parent.setInitializer(call);
			 }else if(get.getParent() instanceof  FunctionCall){
				 FunctionCall call = new FunctionCall();
				 ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
				 arguments.add(get.getElement());
				 call.setArguments(arguments);
				 PropertyGet target = new PropertyGet();
				 Name name = new Name();
				 name.setIdentifier("__g");
				 target.setLeft(get.getTarget());
				 target.setRight(name);
				 call.setTarget(target);
				 FunctionCall  parent = (FunctionCall) get.getParent();
				 ArrayList<AstNode>  parentArguments = new ArrayList<AstNode>();
				 parentArguments.addAll(parent.getArguments());
				 int index = parentArguments.indexOf(get);
				 parentArguments.set(index, call);
				 parent.setArguments(parentArguments);
			 }
		 }else if(node instanceof Assignment){
			  Assignment  assignment =  (Assignment) node;
			  if (assignment.getLeft() instanceof PropertyGet) {
				  if(assignment.getParent() instanceof  ExpressionStatement){
					  ExpressionStatement parent = (ExpressionStatement) assignment.getParent();
					  PropertyGet left = (PropertyGet) assignment.getLeft();
					  Name   leftRight = (Name) left.getRight();

					  StringLiteral literal = new StringLiteral();
					  literal.setValue(leftRight.getIdentifier());
					  literal.setQuoteCharacter('"');
					  FunctionCall call = new FunctionCall();
					  ArrayList<AstNode>  arguments = new ArrayList<AstNode>();
					  arguments.add(literal);
					  arguments.add(assignment.getRight());
					  call.setArguments(arguments);
					  call.setTarget(left);
					  leftRight.setIdentifier("__s");
					  parent.setExpression(call);
				   }else if(assignment.getParent() instanceof FunctionCall){
					  FunctionCall call = ((FunctionCall) assignment.getParent());
					  boolean webpack = false;
					  if(call.getTarget() instanceof  Name){
						  if(((Name) call.getTarget()).getIdentifier().contains(WEBPACK_PREFIX)){
							  webpack = true;
						  }
					  }
					  if(!webpack){
						   String  tips = "please remove bad code sample: \n function(a=b) \nedit this code like below and remove warning:\n a=b;\nfunction(a)";
						   tips +=  "\nwarning untranformed ( equal assignment in function args ) javascript source.\n please ensure below code not use java object. if none use, has no affect on pure javascript, if not, move equal assignment out function args: \n" + assignment.getParent().toSource();
						   JSTransformer.warn(tips);
					  }
				   }
			  }else{
				  JSTransformer.log("Assignment " + node.toSource()  +   assignment.getParent().getClass());
			  }
			 
        }else {
        	JSTransformer.log("end visit" + node.getClass()  + "  " + node.toSource());
  	  }
      return true;
    }
  }