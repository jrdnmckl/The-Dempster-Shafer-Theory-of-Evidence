/**
* Agent Development Environment (ADE)
*
* @version 1.0
* @author Rehj Cantrell
* 
* Copyright 1997-2013 Rehj Cantrell and HRILab (hrilab.org)
* All rights reserved. Do not copy and use without permission.
* For questions contact Rehj Cantrell at rcantrel@indiana.edu.
*/

package com.dshafer;
import com.dshafer.*;

/** A Type is an allowed dialogue act. */

public enum Type {
    STATEMENT,QUESTIONYN,QUESTIONWH,
    REPLY,REPLYY,REPLYN,ACK,
    INSTRUCT,CMDACCEPT,CMDREJECT,
    UNKNOWN, STARTSEQ, ENDSEQ, 
    GETVISUAL, CLARIFICATIONREQUEST

}
