<script setup>
import { ref } from "vue";
import { reactive } from "vue";
import qs from "query-string";
import service from "@/util/request";

// async function courseGetAllService() {
//     //return await axios.get('https://mock.apifox.com/m2/4461960-4108146-default/175216009/')
//     return service.get('/teacher/apply')
//         .then(result => {
//             return result.data;
//         }).catch(err => {
//             console.log(err);
//         });
// }

// async function teacherIDsGetService(teacherName) {
//     //return axios.get('https://mock.apifox.com/m1/4461960-4108146-default/teacher/teachers',{params:{...teacherName.value}})
//     //return service.get('/teacher/teachers',{params:{...teacherName.value}})
//     console.log('/teacher/teachers?teacherName'+'?'+params);
//     const params = qs.stringify({ teacherName: 张三}) // a=1&b=2
//     // return url + '?' + params
//     return service.get('/teacher/teachers?teacherName'+'?'+params)
//     .then(result=>{
//         return result.data;
//     }).catch(err=>{
//         console.log(err);
//     })
// }

// async function classIDsGetService(className) {
//     return service.get('/teacher/classes',{params:{...className.value}})
//     .then(result=>{
//         return result.data;
//     }).catch(err=>{
//         console.log(err);
//     })
// }

// const courseAddService = (courseData => {
//     return service.post('/teacher/apply', courseData);
// })

const dataList = ref([]);

//分页数据模型
const pageNum = ref(1); //当前有效页
const pageSize = ref(10); //每页的数据条数
const totalLine = ref(50); //总数据条数
const small = ref(false);
const background = ref(false);
const disabled = ref(false);

const handleSizeChange = val => {
    console.log(`${val} items per page`);
    pageSize.value = val; // 更新每页数据条数
    search(); // 根据新的每页数据条数获取数据
};
const handleCurrentChange = val => {
    console.log(`current page: ${val}`);
    pageNum.value = val; // 更新当前页码
    search(); // 根据新的当前页码获取数据
};

const searchConditions = reactive({
    status: "",
    page: "", //分页查询的页码
    pageSize: "" //每页记录数
});

const getAllCourse = async function () {
    return service
        .get("/teacher/apply")
        .then(result => {
            //return result.data;
            totalLine.value = result.data.total;
            dataList.value = result.data.rows;
        })
        .catch(err => {
            console.log(err);
        });
    //let result = await courseGetAllService();

    //console.log('get the list')
};

getAllCourse();

//处理用户搜索
const state = ref(); //搜索时选中的状态
const search = async function () {
    // let params = {
    //     pageNum: pageNum,
    //     pageSize: pageSize,
    //     //state: state.value ? state.value : null
    //     state: searchConditions.status ? searchConditions.status : null
    // }
    // let result = await courseSearchStatusService(params);
    // //渲染视图
    // totalLine.value = result.data.total;
    // dataList.value = result.data.rows;
    // console.log('Search button clicked!');
    // console.log(searchConditions.status);

    const params = [];
    params.push(`status=${searchConditions.status}`);
    params.push(`page=${pageNum.value}`);
    params.push(`pageSize=${pageSize.value}`);

    const queryString = params.join("&");
    console.log("/teacher/apply" + "?" + queryString);

    return service
        .get("/teacher/apply" + "?" + queryString)
        .then(result => {
            //return result.data;//渲染视图
            totalLine.value = result.data.total;
            dataList.value = result.data.rows;
        })
        .catch(err => {
            console.log(err);
        });
};

//列表里“查看详情”按钮
const detailedRow = ref({});
const dialogVisible = ref(false);
const handleDetailClick = curRow => {
    console.log("click");
    detailedRow.value = curRow;
    dialogVisible.value = true;
    console.log(detailedRow.value);
};

//添加课程数据模型
const courseModel = ref({
    courseNumber: "",
    courseName: "",
    storage: "",
    courseHour: "",
    term: "",
    type: "",
    faculty: "",
    teacherIds: "",
    classIds: "",
    credit: ""
});
const blurredSearchModel = ref({
    teacherName: "",
    className: ""
});

//添加申请输入校验
const rules = {
    courseNumber: [
        { required: true, message: "请输入课程编号", trigger: "blur" }
    ],
    courseName: [{ required: true, message: "请输入课程名称", trigger: "blur" }],
    courseStorage: [{ required: true, message: "请输入课容量", trigger: "blur" }],
    courseHour: [{ required: true, message: "请输入课时数", trigger: "blur" }],
    type: [{ required: true, message: "请输入课程类别", trigger: "blur" }],
    faculty: [{ required: true, message: "请输入开课学院", trigger: "blur" }],
    teacherIds: [{ required: true, message: "请输入授课教师ID", trigger: "blur" }],
    classIds: [{ required: true, message: "请输入上课班级ID", trigger: "blur" }],
    credit: [{ required: true, message: "请输入课程学分", trigger: "blur" }]
};

//教师模糊查询
const teacherSearchResultList = ref([]);
const handleTeacherSearch = async function () {
    console.log(blurredSearchModel.value.teacherName);
    const params = qs.stringify({
        teacherName: blurredSearchModel.value.teacherName
            ? blurredSearchModel.value.teacherName
            : null
    });
    console.log("/teacher/apply/teachers" + "?" + params);

    return service
        .get("/teacher/apply/teachers" + "?" + params)
        .then(result => {
            //return result.data;//渲染视图
            teacherSearchResultList.value = result.data.teacherList;
        })
        .catch(err => {
            console.log(err);
        });
};

//班级名模糊查询
const classSearchResultList = ref([]);
const handleClassSearch = async function () {
    // let params = {
    //     //blurredClass: blurredSearchModel.value.className?blurredSearchModel.value.className : null
    //     blurredClass: '711'
    // }
    // let result = await classIDsGetService(params);

    console.log(blurredSearchModel.value.className);
    const params = qs.stringify({
        className: blurredSearchModel.value.className
            ? blurredSearchModel.value.className
            : null
    });
    console.log("/teacher/apply/classes" + "?" + params);

    return service
        .get("/teacher/apply/classes" + "?" + params)
        .then(result => {
            //return result.data;
            classSearchResultList.value = result.data.classList;
        })
        .catch(err => {
            console.log(err);
        });

    //渲染视图

    //console.log('Search teacher button clicked!');
};

//添加课程
const addCourse = async () => {
    let result = await service.post("/teacher/apply", courseModel.value);
    //let result = await courseAddService(courseModel.value);
    ElMessage.success(result.msg == "success" ? result.msg : "添加成功");
};

//抽屉
import { ElDrawer, ElMessage, ElMessageBox } from "element-plus";

let timer;
const dialog = ref(false);
const loading = ref(false);

const onClick = () => {
    handleApplyCourseClose();
    addCourse();
    loading.value = true;
    setTimeout(() => {
        loading.value = false;
        dialog.value = false;
    }, 400);
};

const handleApplyCourseClose = done => {
    if (loading.value) {
        return;
    }
    ElMessageBox.confirm("确认提交吗?")
        .then(() => {
            loading.value = true;
            timer = setTimeout(() => {
                done();
                // 动画关闭需要一定的时间
                setTimeout(() => {
                    loading.value = false;
                }, 400);
            }, 2000);
            this.courseModel = {
                courseNumber: "",
                courseName: "",
                courseStorage: "",
                courseHour: "",
                type: "",
                faculty: "",
                teacherIds: "",
                classIds: "",
                credit: ""
            };
            this.blurredSearchModel = {
                teacherName: "",
                classeName: ""
            };
        })
        .catch(() => {
            // catch error
        });

};


const handleApplyCourseClose2 = done => {
    if (loading.value) {
        return;
    }
    ElMessageBox.confirm("确认关闭吗?你所做的更改可能不会保存。")
        .then(() => {
            //loading.value = true;
            timer = setTimeout(() => {
                done();
                //  动画关闭需要一定的时间
                // setTimeout(() => {
                //     loading.value = false;
                // }, 400);
            }, 2000);
            this.courseModel = {
                courseNumber: "",
                courseName: "",
                courseStorage: "",
                courseHour: "",
                type: "",
                faculty: "",
                teacherIds: "",
                classIds: "",
                credit: ""
            };
            this.blurredSearchModel = {
                teacherName: "",
                classeName: ""
            };
        })
        .catch(() => {
            // catch error
        });

};

const cancelForm = () => {
    
    loading.value = false;
    dialog.value = false;
    clearTimeout(timer);
    
};
</script>



<template>
    <el-card style="max-width: 1200px">
        <div class="card-header">
            <span>课程管理</span>
            <el-button type="primary" @click="dialog = true">申请课程</el-button>
        </div>

        <div>
            <hr />
        </div>

        <el-form :inline="true" :model="searchConditions" class="demo-form-inline">
            <!-- <el-form-item label="">
          <el-input v-model="formInline.user" placeholder="Approved by" clearable />
      </el-form-item>-->
            <el-form-item label="审核状态">
                <el-select v-model="searchConditions.status" placeholder="审核状态" clearable>
                    <el-option label="待审核" value="1" />
                    <el-option label="已通过" value="2" />
                    <el-option label="已驳回" value="3" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="search">查询</el-button>
            </el-form-item>
        </el-form>

        <!-- 列表展示 -->
        <el-table :data="dataList" style="width: 100%">
            <el-table-column prop="courseNumber" label="课程编号" width="180" />
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="courseStatus" label="审核状态" />
            <el-table-column prop="courseStorage" label="课容量" />
            <el-table-column prop="courseHour" label="课时数" />
            <el-table-column prop="teachers" label="授课教师" />
            <el-table-column label="操作">
                <template #default="{ row }">
                    <el-button link type="primary" size="small" @click="handleDetailClick(row)">详情</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 详情浮窗 -->
        <el-dialog v-model="dialogVisible" title="课程申请" width="30%">
            <el-form :model="detailedRow.value" label-width="100px" style="padding-right:30px">
                <el-form-item label="课程编号" prop="courseNumber">
                    <span>{{ detailedRow.courseNumber }}</span>
                </el-form-item>
                <el-form-item label="课程名称" prop="courseName">
                    <span>{{ detailedRow.courseName }}</span>
                </el-form-item>
                <el-form-item label="课容量" prop="courseStorage">
                    <span>{{ detailedRow.courseStorage }}</span>
                </el-form-item>
                <el-form-item label="课时数" prop="courseHour">
                    <span>{{ detailedRow.courseHour }}</span>
                </el-form-item>
                <el-form-item label="课程类别" prop="type">
                    <span>{{ detailedRow.type }}</span>
                </el-form-item>
                <el-form-item label="开课单位" prop="faculty">
                    <span>{{ detailedRow.faculty }}</span>
                </el-form-item>
                <el-form-item label="授课教师" prop="teachers">
                    <span>{{ detailedRow.teachers }}</span>
                </el-form-item>
                <el-form-item label="上课班级" prop="classes">
                    <span>{{ detailedRow.classes }}</span>
                </el-form-item>
                <el-form-item label="课程学分" prop="credit">
                    <span>{{ detailedRow.credit }}</span>
                </el-form-item>
                <el-form-item label="审核状态" prop="status">
                    <span>{{ detailedRow.courseStatus }}</span>
                </el-form-item>
                <el-form-item label="提交时间" prop="committime">
                    <span>{{ detailedRow.commitTime }}</span>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 课程申请抽屉 -->
        <el-drawer v-model="dialog" title="课程信息填写" :before-close="handleApplyCourseClose2" direction="ltr"
            class="demo-drawer">
            <div class="demo-drawer__content">
                <el-form :model="courseModel" :rules="rules">
                    <el-form-item label="课程编号" :label-width="formLabelWidth" prop="courseNumber">
                        <el-input v-model="courseModel.courseNumber" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="课程名称" :label-width="formLabelWidth" prop="courseName">
                        <el-input v-model="courseModel.courseName" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="课容量" :label-width="formLabelWidth" prop="courseStorage">
                        <el-input v-model="courseModel.courseStorage" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="课时数" :label-width="formLabelWidth" prop="courseHour">
                        <el-input v-model="courseModel.courseHour" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="课程类别" :label-width="formLabelWidth" prop="type">
                        <el-input v-model="courseModel.type" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="开课单位" :label-width="formLabelWidth" prop="faculty">
                        <el-input v-model="courseModel.faculty" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="教师ID" :label-width="formLabelWidth" prop="teacherIds">
                        <el-input v-model="courseModel.teacherIds" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="班级ID" :label-width="formLabelWidth" prop="classIds">
                        <el-input v-model="courseModel.classIds" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="课程学分" :label-width="formLabelWidth" prop="credit">
                        <el-input v-model="courseModel.credit" minlength="1" maxlength="10"></el-input>
                    </el-form-item>
                </el-form>
                <el-form :model="blurredSearchModel" label-width="100px" style="padding-right:30px">
                    <el-form-item label="快捷搜索教师" prop="teacherName">
                        <el-input v-model="blurredSearchModel.teacherName" @input="handleTeacherSearch"
                            placeholder="输入教师名以模糊搜索"></el-input>
                        <el-select v-model="teacherSearchResultList" placeholder="请选择搜索结果">
                            <el-option v-for="item in teacherSearchResultList" :key="item.id" :label="item.id"
                                :value="item.id">{{ item.id }} - {{ item.username }} - {{ item.teacherName
                                }}</el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="快捷搜索班级" prop="className">
                        <el-input v-model="blurredSearchModel.className" @input="handleClassSearch"
                            placeholder="输入班级名以模糊搜索"></el-input>
                        <el-select v-model="classSearchResultList" placeholder="请选择搜索结果">
                            <el-option v-for="item in classSearchResultList" :key="item.id" :label="item.id"
                                :value="item.id">{{
                                    item.id }} - {{ item.className }}</el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div class="demo-drawer__footer">
                    <el-button @click="cancelForm">取消</el-button>
                    <el-button type="primary" :loading="loading" @click="onClick">{{ loading ? '提交中...' : '提交'
                        }}</el-button>
                </div>
            </div>
        </el-drawer>

        <!-- 分页条 -->
        <div class="el-p">
            <div class="demonstration"></div>
            <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[1, 10, 15, 20]"
                :small="small" :disabled="disabled" :background="background"
                layout="total, sizes, prev, pager, next, jumper" :total="60" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>
    </el-card>
</template>

<style>
.demo-form-inline .el-input {
    --el-input-width: 220px;
}

.demo-form-inline .el-select {
    --el-select-width: 220px;
}

.el-p {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

.card-header {
    display: flex;
    justify-content: space-between;
}

.input-with-select .el-input-group__prepend {
    background-color: var(--el-fill-color-blank);
}
</style>