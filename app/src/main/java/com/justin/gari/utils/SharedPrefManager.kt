package com.justin.gari.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(var _context: Context) {
    var PRIVATE_MODE = 0
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var SHARED_PREF_NAME = "GariSharedPrefData"


    var USERID= "userId"
    var ROLEID= "roleId"
    var ROLENAME= "roleName"
    var ROLEDESCRIPTION= "roleDescription"
    var FIRSTNAME = "firstName"
    var LASTNAME = "lastname"
    var EMAIL= "email"
    var USERPROFILEPHOTO = "userProfilePhoto"
    var SWITCHEDTHEME ="switchedTheme"
    var ONBOARDING ="onBoarding"

    var EXCLUDED_KEYS = setOf(ONBOARDING)


    // Constructor
    init {
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    @JvmName("getUSERID1")
    fun getUSERID(): String? {
        return if (pref.getString(USERID, "") != "") pref.getString(
            USERID,
            ""
        ) else ""
    }

    @JvmName("setUSERID1")
    fun setUSERID(userId: String?) {
        editor.putString(USERID, userId)
        editor.apply()
    }

    @JvmName("getROLEID1")
    fun getROLEID(): String? {
        return if (pref.getString(ROLEID, "") != "") pref.getString(
            ROLEID,
            ""
        ) else ""
    }

    @JvmName("setROLEID1")
    fun setROLEID(roleId: String?) {
        editor.putString(ROLEID, roleId)
        editor.apply()
    }

    @JvmName("getFIRSTNAME1")
    fun getFIRSTNAME(): String? {
        return if (pref.getString(FIRSTNAME, "") != "") pref.getString(
            FIRSTNAME,
            ""
        ) else ""
    }

    @JvmName("setFIRSTNAME1")
    fun setFIRSTNAME(firstName: String?) {
        editor.putString(FIRSTNAME, firstName)
        editor.apply()
    }
    
    @JvmName("getLASTNAME1")
    fun getLASTNAME(): String? {
        return if (pref.getString(LASTNAME, "") != "") pref.getString(
            LASTNAME,
            ""
        ) else ""
    }
    
    @JvmName("setLASTNAME1")
    fun setLASTNAME(lastName: String?) {
        editor.putString(LASTNAME, lastName)
        editor.apply()
    }

    @JvmName("getEMAIL1")
    fun getEMAIL(): String? {
        return if (pref.getString(EMAIL, "") != "") pref.getString(
            EMAIL,
            ""
        ) else ""
    }

    @JvmName("setEMAIL1")
    fun setEMAIL(email: String?) {
        editor.putString(EMAIL, email)
        editor.apply()
    }

    @JvmName("getUSERPROFILEPHOTO1")
    fun getUSERPROFILEPHOTO(): String? {
        return if (pref.getString(USERPROFILEPHOTO, null) != null) pref.getString(
            USERPROFILEPHOTO,
            null
        ) else null
    }

    @JvmName("setUSERPROFILEPHOTO1")
    fun setUSERPROFILEPHOTO(userProfilePhoto: String?) {
        editor.putString(USERPROFILEPHOTO, userProfilePhoto)
        editor.apply()
    }

    @JvmName("getROLENAME1")
    fun getROLENAME(): String? {
        return if (pref.getString(ROLENAME, "") != "") pref.getString(
            ROLENAME,
            ""
        ) else ""
    }

    @JvmName("setROLENAME1")
    fun setROLENAME(roleName: String?) {
        editor.putString(ROLENAME, roleName)
        editor.apply()
    }

    @JvmName("getROLEDESCRIPTION1")
    fun getROLEDESCRIPTION(): String? {
        return if (pref.getString(ROLEDESCRIPTION, "") != "") pref.getString(
            ROLEDESCRIPTION,
            ""
        ) else ""
    }

    @JvmName("setROLEDESCRIPTION1")
    fun setROLEDESCRIPTION(roleDescription: String?) {
        editor.putString(ROLEDESCRIPTION, roleDescription)
        editor.apply()
    }

    @JvmName("getSWITCHEDTHEME1")
    fun getSWITCHEDTHEME(): Boolean {
        return pref.getBoolean(SWITCHEDTHEME, false)
    }

    @JvmName("setSWITCHEDTHEME1")
    fun setSWITCHEDTHEME(switchedTheme: Boolean) {
        editor.putBoolean(SWITCHEDTHEME, switchedTheme)
        editor.apply()
    }

    @JvmName("getONBOARDING1")
    fun getONBOARDING(): Boolean {
        return pref.getBoolean(ONBOARDING, false)
    }

    @JvmName("setONBOARDING1")
    fun setONBOARDING(onBoarding: Boolean) {
        editor.putBoolean(ONBOARDING, onBoarding)
        editor.apply()
    }

//    fun clearAllData() {
//        editor.clear().apply()
//    }

    fun clearAllDataExcept(vararg keys: String) {
        val excludedKeys = EXCLUDED_KEYS.union(keys.toSet())
        val allKeys = pref.all.keys
        for (key in allKeys) {
            if (!excludedKeys.contains(key)) {
                editor.remove(key)
            }
        }
        editor.apply()
    }
}
