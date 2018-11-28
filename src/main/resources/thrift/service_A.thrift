namespace java com.young.thrift.module

struct Student{
    1:required string studentName, #学生姓名
    2:required string sex,         #性别
    3:required i32 age,            #学生年龄
}
struct Grade{
    1:required string gradeName, #年假名称
    2:required list<Student> allStudents, #所有学生
}
struct school {
    1:required string schoolName,
    2:required i64    age,
    3:required list<string> major, #所有专业
    4:required list<Grade> allClass, #所有班级
}