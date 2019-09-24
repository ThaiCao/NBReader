//
// Created by 陈广祥 on 2019-09-19.
//

#include "AndroidUtil.h"

JavaVM *AndroidUtil::sJavaVM = 0;

const std::string PKG_NAME = "com/example/newbiechen/nbreader/";

JavaClass AndroidUtil::Class_String("java/lang/String");

JavaClass AndroidUtil::Class_NativeFormatPlugin(
        PKG_NAME + "ui/component/book/plugin/NativeFormatPlugin");

JavaClass AndroidUtil::Class_FormatPluginManager(
        PKG_NAME + "ui/component/book/plugin/FormatPluginManager");

JavaClass AndroidUtil::Class_BookModel(PKG_NAME + "ui/component/book/entity/BookModel");

JavaClass AndroidUtil::Class_Book(PKG_NAME + "data/entity/BookEntity");

// 初始化静态成员。。
std::shared_ptr<StringMethod> AndroidUtil::Method_NativeFormatPlugin_getSupportTypeByStr;
std::shared_ptr<ObjectMethod> AndroidUtil::Method_BookModel_getBook;
std::shared_ptr<StringMethod> AndroidUtil::Method_Book_getTitle;
std::shared_ptr<StringMethod> AndroidUtil::Method_Book_getUrl;
std::shared_ptr<StringMethod> AndroidUtil::Method_Book_getEncoding;
std::shared_ptr<StringMethod> AndroidUtil::Method_Book_getLang;
std::shared_ptr<StringMethod> Method_String_toLowerCase;
std::shared_ptr<StringMethod> Method_String_toUpperCase;

JNIEnv *AndroidUtil::getEnv() {
    JNIEnv *env;
    sJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    return env;
}

bool AndroidUtil::init(JavaVM *jvm) {
    sJavaVM = jvm;

    // string
    Method_String_toLowerCase = new StringMethod(Class_String, "toLowerCase", "()");
    Method_String_toUpperCase = new StringMethod(Class_String, "toUpperCase", "()");

    Method_NativeFormatPlugin_getSupportTypeByStr = std::make_shared<StringMethod>(Class_NativeFormatPlugin,
                                                                                   "getSupportTypeByStr",
                                                                                   "()");
    Method_BookModel_getBook = std::make_shared<ObjectMethod>(Class_BookModel, "getBook", Class_Book, "()");

    /*Book*/
    Method_Book_getTitle = std::make_shared<StringMethod>(Class_Book, "getTitle", "()");
    Method_Book_getUrl = std::make_shared<StringMethod>(Class_Book, "getUrl", "()");
    Method_Book_getEncoding = std::make_shared<StringMethod>(Class_Book, "getEncoding", "()");
    Method_Book_getLang = std::make_shared<StringMethod>(Class_Book, "getLang", "()");
    return true;
}

jstring AndroidUtil::toJString(JNIEnv *env, const std::string &str) {
    if (str.empty()) {
        return 0;
    }
    return env->NewStringUTF(str.c_str());
}

std::string AndroidUtil::toCString(JNIEnv *env, jstring from) {
    if (from == 0) {
        return std::string();
    }
    const char *data = env->GetStringUTFChars(from, 0);
    const std::string result(data);
    env->ReleaseStringUTFChars(from, data);
    return result;
}