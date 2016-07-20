package com.faayda.models;

/**
 * Created by vinove on 7/28/2015.
 */
public final class TermsAndPolicyResponse {
    //{"code" : "200", "termsnpolicy" :[{"termsServices": "Loremstry.",
    // "Policy" : "Lorem Ipsum isg and typesetting industry."}]}
  /*  public int code;
    public  termsServices ;
    public String Policy;*/

    public int code;
    public TermsPolicyModel[] termsnpolicy;
}
