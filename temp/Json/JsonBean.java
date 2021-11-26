
	private String swagger;
	private Info info;
	private String host;
	private String basePath;
	private ArrayList<Object> schemes;
	private ArrayList<String> consumes;
	private ArrayList<String> produces;
	private Paths paths;
	private Definitions definitions;

	public static class Info {
		private String description;
		private String version;
		private String title;
		private String termsOfService;
		private Contact contact;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTermsOfService() {
			return termsOfService;
		}

		public void setTermsOfService(String termsOfService) {
			this.termsOfService = termsOfService;
		}

		public Contact getContact() {
			return contact;
		}

		public void setContact(Contact contact) {
			this.contact = contact;
		}
	}

	public static class Contact {
		private String name;
		private String url;
		private String email;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static class Paths {
		private /question/feedback/pageManagerInSalesQuestion /question/feedback/pageManagerInSalesQuestion;

		public /question/feedback/pageManagerInSalesQuestion get/question/feedback/pageManagerInSalesQuestion() {
			return /question/feedback/pageManagerInSalesQuestion;
		}

		public void set/question/feedback/pageManagerInSalesQuestion(/question/feedback/pageManagerInSalesQuestion /question/feedback/pageManagerInSalesQuestion) {
			this./question/feedback/pageManagerInSalesQuestion = /question/feedback/pageManagerInSalesQuestion;
		}
	}

	public static class /question/feedback/pageManagerInSalesQuestion {
		private Get get;

		public Get getGet() {
			return get;
		}

		public void setGet(Get get) {
			this.get = get;
		}
	}

	public static class Get {
		private ArrayList<String> tags;
		private String summary;
		private String operationId;
		private ArrayList<String> produces;
		private ArrayList<Parameters> parameters;
		private Responses responses;

		public ArrayList<String> getTags() {
			return tags;
		}

		public void setTags(ArrayList<String> tags) {
			this.tags = tags;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getOperationId() {
			return operationId;
		}

		public void setOperationId(String operationId) {
			this.operationId = operationId;
		}

		public ArrayList<String> getProduces() {
			return produces;
		}

		public void setProduces(ArrayList<String> produces) {
			this.produces = produces;
		}

		public ArrayList<Parameters> getParameters() {
			return parameters;
		}

		public void setParameters(ArrayList<Parameters> parameters) {
			this.parameters = parameters;
		}

		public Responses getResponses() {
			return responses;
		}

		public void setResponses(Responses responses) {
			this.responses = responses;
		}
	}

	public static class Parameters {
		private String name;
		private String in;
		private String description;
		private boolean required;
		private String type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIn() {
			return in;
		}

		public void setIn(String in) {
			this.in = in;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	public static class Responses {
		private 200 200;
		private 401 401;
		private 403 403;
		private 404 404;

		public 200 get200() {
			return 200;
		}

		public void set200(200 200) {
			this.200 = 200;
		}

		public 401 get401() {
			return 401;
		}

		public void set401(401 401) {
			this.401 = 401;
		}

		public 403 get403() {
			return 403;
		}

		public void set403(403 403) {
			this.403 = 403;
		}

		public 404 get404() {
			return 404;
		}

		public void set404(404 404) {
			this.404 = 404;
		}
	}

	public static class 200 {
		private String description;
		private Schema schema;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Schema getSchema() {
			return schema;
		}

		public void setSchema(Schema schema) {
			this.schema = schema;
		}
	}

	public static class Schema {
		private String $ref;

		public String get$ref() {
			return $ref;
		}

		public void set$ref(String $ref) {
			this.$ref = $ref;
		}
	}

	public static class 401 {
		private String description;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class 403 {
		private String description;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class 404 {
		private String description;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class Definitions {
		private JsonResult?Page?QuestionFeedback对象?? JsonResult?Page?QuestionFeedback对象??;
		private Page?QuestionFeedback对象? Page?QuestionFeedback对象?;
		private QuestionFeedback对象 QuestionFeedback对象;
		private QuestionFeedbackPortalFile对象 QuestionFeedbackPortalFile对象;
		private QuestionFeedbackDetail对象 QuestionFeedbackDetail对象;
		private QuestionFeedbackPortalLog对象 QuestionFeedbackPortalLog对象;

		public JsonResult?Page?QuestionFeedback对象?? getJsonResult?Page?QuestionFeedback对象??() {
			return JsonResult?Page?QuestionFeedback对象??;
		}

		public void setJsonResult?Page?QuestionFeedback对象??(JsonResult?Page?QuestionFeedback对象?? JsonResult?Page?QuestionFeedback对象??) {
			this.JsonResult?Page?QuestionFeedback对象?? = JsonResult?Page?QuestionFeedback对象??;
		}

		public Page?QuestionFeedback对象? getPage?QuestionFeedback对象?() {
			return Page?QuestionFeedback对象?;
		}

		public void setPage?QuestionFeedback对象?(Page?QuestionFeedback对象? Page?QuestionFeedback对象?) {
			this.Page?QuestionFeedback对象? = Page?QuestionFeedback对象?;
		}

		public QuestionFeedback对象 getQuestionFeedback对象() {
			return QuestionFeedback对象;
		}

		public void setQuestionFeedback对象(QuestionFeedback对象 QuestionFeedback对象) {
			this.QuestionFeedback对象 = QuestionFeedback对象;
		}

		public QuestionFeedbackPortalFile对象 getQuestionFeedbackPortalFile对象() {
			return QuestionFeedbackPortalFile对象;
		}

		public void setQuestionFeedbackPortalFile对象(QuestionFeedbackPortalFile对象 QuestionFeedbackPortalFile对象) {
			this.QuestionFeedbackPortalFile对象 = QuestionFeedbackPortalFile对象;
		}

		public QuestionFeedbackDetail对象 getQuestionFeedbackDetail对象() {
			return QuestionFeedbackDetail对象;
		}

		public void setQuestionFeedbackDetail对象(QuestionFeedbackDetail对象 QuestionFeedbackDetail对象) {
			this.QuestionFeedbackDetail对象 = QuestionFeedbackDetail对象;
		}

		public QuestionFeedbackPortalLog对象 getQuestionFeedbackPortalLog对象() {
			return QuestionFeedbackPortalLog对象;
		}

		public void setQuestionFeedbackPortalLog对象(QuestionFeedbackPortalLog对象 QuestionFeedbackPortalLog对象) {
			this.QuestionFeedbackPortalLog对象 = QuestionFeedbackPortalLog对象;
		}
	}

	public static class JsonResult?Page?QuestionFeedback对象?? {
		private String type;
		private Properties properties;
		private String title;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	public static class Properties {
		private Code code;
		private Data data;
		private Msg msg;
		private Current current;
		private Pages pages;
		private Records records;
		private Size size;
		private Total total;
		private AttachmentList attachmentList;
		private AuditName auditName;
		private AuditPhoto auditPhoto;
		private AuditSiebleUid auditSiebleUid;
		private BaseFlag baseFlag;
		private ClamantLevel clamantLevel;
		private CreateDate createDate;
		private DetailDtoList detailDtoList;
		private FeedbackDuration feedbackDuration;
		private ImportantLevel importantLevel;
		private QuestionCategory questionCategory;
		private QuestionDesc questionDesc;
		private QuestionPhoto questionPhoto;
		private QuestionStatus questionStatus;
		private RecommendDesc recommendDesc;
		private RecommendFlag recommendFlag;
		private RecordList recordList;
		private SaleName saleName;
		private SaleSiebleUid saleSiebleUid;
		private StoreName storeName;
		private StoreRegion storeRegion;
		private StoreSiebleUid storeSiebleUid;
		private Uid uid;
		private UpdateDate updateDate;
		private VisitUid visitUid;
		private CreateDate createDate;
		private FileId fileId;
		private FileName fileName;
		private FileSize fileSize;
		private FileType fileType;
		private Flag flag;
		private QuestionUid questionUid;
		private Uid uid;
		private UpdateDate updateDate;
		private Url url;
		private AuditDesc auditDesc;
		private AuditRecommendDesc auditRecommendDesc;
		private BaseFlag baseFlag;
		private CreateDate createDate;
		private DetailOpType detailOpType;
		private DiscardReason discardReason;
		private FeedbackDesc feedbackDesc;
		private FeedbackDuration feedbackDuration;
		private PortalNo portalNo;
		private PortalProcessorSiebleUid portalProcessorSiebleUid;
		private PortalUid portalUid;
		private ProofDesc proofDesc;
		private ProofPhoto proofPhoto;
		private QuestionResult questionResult;
		private QuestionUid questionUid;
		private ReturnReason returnReason;
		private SaleName saleName;
		private SaleSiebleUid saleSiebleUid;
		private Uid uid;
		private UpdateDate updateDate;
		private VisitUid visitUid;
		private Createdate createdate;
		private Instid instid;
		private Processname processname;
		private Processnamecn processnamecn;
		private Processnodename processnodename;
		private QuestionUid questionUid;
		private Reason reason;
		private Result result;
		private Startdate startdate;
		private Status status;
		private Uid uid;
		private Usercnname usercnname;
		private Userid userid;
		private Username username;
		private Waitdate waitdate;

		public Code getCode() {
			return code;
		}

		public void setCode(Code code) {
			this.code = code;
		}

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		public Msg getMsg() {
			return msg;
		}

		public void setMsg(Msg msg) {
			this.msg = msg;
		}

		public Current getCurrent() {
			return current;
		}

		public void setCurrent(Current current) {
			this.current = current;
		}

		public Pages getPages() {
			return pages;
		}

		public void setPages(Pages pages) {
			this.pages = pages;
		}

		public Records getRecords() {
			return records;
		}

		public void setRecords(Records records) {
			this.records = records;
		}

		public Size getSize() {
			return size;
		}

		public void setSize(Size size) {
			this.size = size;
		}

		public Total getTotal() {
			return total;
		}

		public void setTotal(Total total) {
			this.total = total;
		}

		public AttachmentList getAttachmentList() {
			return attachmentList;
		}

		public void setAttachmentList(AttachmentList attachmentList) {
			this.attachmentList = attachmentList;
		}

		public AuditName getAuditName() {
			return auditName;
		}

		public void setAuditName(AuditName auditName) {
			this.auditName = auditName;
		}

		public AuditPhoto getAuditPhoto() {
			return auditPhoto;
		}

		public void setAuditPhoto(AuditPhoto auditPhoto) {
			this.auditPhoto = auditPhoto;
		}

		public AuditSiebleUid getAuditSiebleUid() {
			return auditSiebleUid;
		}

		public void setAuditSiebleUid(AuditSiebleUid auditSiebleUid) {
			this.auditSiebleUid = auditSiebleUid;
		}

		public BaseFlag getBaseFlag() {
			return baseFlag;
		}

		public void setBaseFlag(BaseFlag baseFlag) {
			this.baseFlag = baseFlag;
		}

		public ClamantLevel getClamantLevel() {
			return clamantLevel;
		}

		public void setClamantLevel(ClamantLevel clamantLevel) {
			this.clamantLevel = clamantLevel;
		}

		public CreateDate getCreateDate() {
			return createDate;
		}

		public void setCreateDate(CreateDate createDate) {
			this.createDate = createDate;
		}

		public DetailDtoList getDetailDtoList() {
			return detailDtoList;
		}

		public void setDetailDtoList(DetailDtoList detailDtoList) {
			this.detailDtoList = detailDtoList;
		}

		public FeedbackDuration getFeedbackDuration() {
			return feedbackDuration;
		}

		public void setFeedbackDuration(FeedbackDuration feedbackDuration) {
			this.feedbackDuration = feedbackDuration;
		}

		public ImportantLevel getImportantLevel() {
			return importantLevel;
		}

		public void setImportantLevel(ImportantLevel importantLevel) {
			this.importantLevel = importantLevel;
		}

		public QuestionCategory getQuestionCategory() {
			return questionCategory;
		}

		public void setQuestionCategory(QuestionCategory questionCategory) {
			this.questionCategory = questionCategory;
		}

		public QuestionDesc getQuestionDesc() {
			return questionDesc;
		}

		public void setQuestionDesc(QuestionDesc questionDesc) {
			this.questionDesc = questionDesc;
		}

		public QuestionPhoto getQuestionPhoto() {
			return questionPhoto;
		}

		public void setQuestionPhoto(QuestionPhoto questionPhoto) {
			this.questionPhoto = questionPhoto;
		}

		public QuestionStatus getQuestionStatus() {
			return questionStatus;
		}

		public void setQuestionStatus(QuestionStatus questionStatus) {
			this.questionStatus = questionStatus;
		}

		public RecommendDesc getRecommendDesc() {
			return recommendDesc;
		}

		public void setRecommendDesc(RecommendDesc recommendDesc) {
			this.recommendDesc = recommendDesc;
		}

		public RecommendFlag getRecommendFlag() {
			return recommendFlag;
		}

		public void setRecommendFlag(RecommendFlag recommendFlag) {
			this.recommendFlag = recommendFlag;
		}

		public RecordList getRecordList() {
			return recordList;
		}

		public void setRecordList(RecordList recordList) {
			this.recordList = recordList;
		}

		public SaleName getSaleName() {
			return saleName;
		}

		public void setSaleName(SaleName saleName) {
			this.saleName = saleName;
		}

		public SaleSiebleUid getSaleSiebleUid() {
			return saleSiebleUid;
		}

		public void setSaleSiebleUid(SaleSiebleUid saleSiebleUid) {
			this.saleSiebleUid = saleSiebleUid;
		}

		public StoreName getStoreName() {
			return storeName;
		}

		public void setStoreName(StoreName storeName) {
			this.storeName = storeName;
		}

		public StoreRegion getStoreRegion() {
			return storeRegion;
		}

		public void setStoreRegion(StoreRegion storeRegion) {
			this.storeRegion = storeRegion;
		}

		public StoreSiebleUid getStoreSiebleUid() {
			return storeSiebleUid;
		}

		public void setStoreSiebleUid(StoreSiebleUid storeSiebleUid) {
			this.storeSiebleUid = storeSiebleUid;
		}

		public Uid getUid() {
			return uid;
		}

		public void setUid(Uid uid) {
			this.uid = uid;
		}

		public UpdateDate getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(UpdateDate updateDate) {
			this.updateDate = updateDate;
		}

		public VisitUid getVisitUid() {
			return visitUid;
		}

		public void setVisitUid(VisitUid visitUid) {
			this.visitUid = visitUid;
		}

		public CreateDate getCreateDate() {
			return createDate;
		}

		public void setCreateDate(CreateDate createDate) {
			this.createDate = createDate;
		}

		public FileId getFileId() {
			return fileId;
		}

		public void setFileId(FileId fileId) {
			this.fileId = fileId;
		}

		public FileName getFileName() {
			return fileName;
		}

		public void setFileName(FileName fileName) {
			this.fileName = fileName;
		}

		public FileSize getFileSize() {
			return fileSize;
		}

		public void setFileSize(FileSize fileSize) {
			this.fileSize = fileSize;
		}

		public FileType getFileType() {
			return fileType;
		}

		public void setFileType(FileType fileType) {
			this.fileType = fileType;
		}

		public Flag getFlag() {
			return flag;
		}

		public void setFlag(Flag flag) {
			this.flag = flag;
		}

		public QuestionUid getQuestionUid() {
			return questionUid;
		}

		public void setQuestionUid(QuestionUid questionUid) {
			this.questionUid = questionUid;
		}

		public Uid getUid() {
			return uid;
		}

		public void setUid(Uid uid) {
			this.uid = uid;
		}

		public UpdateDate getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(UpdateDate updateDate) {
			this.updateDate = updateDate;
		}

		public Url getUrl() {
			return url;
		}

		public void setUrl(Url url) {
			this.url = url;
		}

		public AuditDesc getAuditDesc() {
			return auditDesc;
		}

		public void setAuditDesc(AuditDesc auditDesc) {
			this.auditDesc = auditDesc;
		}

		public AuditRecommendDesc getAuditRecommendDesc() {
			return auditRecommendDesc;
		}

		public void setAuditRecommendDesc(AuditRecommendDesc auditRecommendDesc) {
			this.auditRecommendDesc = auditRecommendDesc;
		}

		public BaseFlag getBaseFlag() {
			return baseFlag;
		}

		public void setBaseFlag(BaseFlag baseFlag) {
			this.baseFlag = baseFlag;
		}

		public CreateDate getCreateDate() {
			return createDate;
		}

		public void setCreateDate(CreateDate createDate) {
			this.createDate = createDate;
		}

		public DetailOpType getDetailOpType() {
			return detailOpType;
		}

		public void setDetailOpType(DetailOpType detailOpType) {
			this.detailOpType = detailOpType;
		}

		public DiscardReason getDiscardReason() {
			return discardReason;
		}

		public void setDiscardReason(DiscardReason discardReason) {
			this.discardReason = discardReason;
		}

		public FeedbackDesc getFeedbackDesc() {
			return feedbackDesc;
		}

		public void setFeedbackDesc(FeedbackDesc feedbackDesc) {
			this.feedbackDesc = feedbackDesc;
		}

		public FeedbackDuration getFeedbackDuration() {
			return feedbackDuration;
		}

		public void setFeedbackDuration(FeedbackDuration feedbackDuration) {
			this.feedbackDuration = feedbackDuration;
		}

		public PortalNo getPortalNo() {
			return portalNo;
		}

		public void setPortalNo(PortalNo portalNo) {
			this.portalNo = portalNo;
		}

		public PortalProcessorSiebleUid getPortalProcessorSiebleUid() {
			return portalProcessorSiebleUid;
		}

		public void setPortalProcessorSiebleUid(PortalProcessorSiebleUid portalProcessorSiebleUid) {
			this.portalProcessorSiebleUid = portalProcessorSiebleUid;
		}

		public PortalUid getPortalUid() {
			return portalUid;
		}

		public void setPortalUid(PortalUid portalUid) {
			this.portalUid = portalUid;
		}

		public ProofDesc getProofDesc() {
			return proofDesc;
		}

		public void setProofDesc(ProofDesc proofDesc) {
			this.proofDesc = proofDesc;
		}

		public ProofPhoto getProofPhoto() {
			return proofPhoto;
		}

		public void setProofPhoto(ProofPhoto proofPhoto) {
			this.proofPhoto = proofPhoto;
		}

		public QuestionResult getQuestionResult() {
			return questionResult;
		}

		public void setQuestionResult(QuestionResult questionResult) {
			this.questionResult = questionResult;
		}

		public QuestionUid getQuestionUid() {
			return questionUid;
		}

		public void setQuestionUid(QuestionUid questionUid) {
			this.questionUid = questionUid;
		}

		public ReturnReason getReturnReason() {
			return returnReason;
		}

		public void setReturnReason(ReturnReason returnReason) {
			this.returnReason = returnReason;
		}

		public SaleName getSaleName() {
			return saleName;
		}

		public void setSaleName(SaleName saleName) {
			this.saleName = saleName;
		}

		public SaleSiebleUid getSaleSiebleUid() {
			return saleSiebleUid;
		}

		public void setSaleSiebleUid(SaleSiebleUid saleSiebleUid) {
			this.saleSiebleUid = saleSiebleUid;
		}

		public Uid getUid() {
			return uid;
		}

		public void setUid(Uid uid) {
			this.uid = uid;
		}

		public UpdateDate getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(UpdateDate updateDate) {
			this.updateDate = updateDate;
		}

		public VisitUid getVisitUid() {
			return visitUid;
		}

		public void setVisitUid(VisitUid visitUid) {
			this.visitUid = visitUid;
		}

		public Createdate getCreatedate() {
			return createdate;
		}

		public void setCreatedate(Createdate createdate) {
			this.createdate = createdate;
		}

		public Instid getInstid() {
			return instid;
		}

		public void setInstid(Instid instid) {
			this.instid = instid;
		}

		public Processname getProcessname() {
			return processname;
		}

		public void setProcessname(Processname processname) {
			this.processname = processname;
		}

		public Processnamecn getProcessnamecn() {
			return processnamecn;
		}

		public void setProcessnamecn(Processnamecn processnamecn) {
			this.processnamecn = processnamecn;
		}

		public Processnodename getProcessnodename() {
			return processnodename;
		}

		public void setProcessnodename(Processnodename processnodename) {
			this.processnodename = processnodename;
		}

		public QuestionUid getQuestionUid() {
			return questionUid;
		}

		public void setQuestionUid(QuestionUid questionUid) {
			this.questionUid = questionUid;
		}

		public Reason getReason() {
			return reason;
		}

		public void setReason(Reason reason) {
			this.reason = reason;
		}

		public Result getResult() {
			return result;
		}

		public void setResult(Result result) {
			this.result = result;
		}

		public Startdate getStartdate() {
			return startdate;
		}

		public void setStartdate(Startdate startdate) {
			this.startdate = startdate;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public Uid getUid() {
			return uid;
		}

		public void setUid(Uid uid) {
			this.uid = uid;
		}

		public Usercnname getUsercnname() {
			return usercnname;
		}

		public void setUsercnname(Usercnname usercnname) {
			this.usercnname = usercnname;
		}

		public Userid getUserid() {
			return userid;
		}

		public void setUserid(Userid userid) {
			this.userid = userid;
		}

		public Username getUsername() {
			return username;
		}

		public void setUsername(Username username) {
			this.username = username;
		}

		public Waitdate getWaitdate() {
			return waitdate;
		}

		public void setWaitdate(Waitdate waitdate) {
			this.waitdate = waitdate;
		}
	}

	public static class Code {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}
	}

	public static class Data {
		private String description;
		private boolean allowEmptyValue;
		private String $ref;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public String get$ref() {
			return $ref;
		}

		public void set$ref(String $ref) {
			this.$ref = $ref;
		}
	}

	public static class Msg {
		private String type;
		private String description;
		private boolean allowEmptyValue;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}
	}

	public static class Page?QuestionFeedback对象? {
		private String type;
		private Properties properties;
		private String title;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	public static class Current {
		private String type;
		private String format;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Pages {
		private String type;
		private String format;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Records {
		private String type;
		private Items items;
		private String refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}

		public String getRefType() {
			return refType;
		}

		public void setRefType(String refType) {
			this.refType = refType;
		}
	}

	public static class Items {
		private String $ref;
		private String $ref;
		private String $ref;
		private String $ref;

		public String get$ref() {
			return $ref;
		}

		public void set$ref(String $ref) {
			this.$ref = $ref;
		}

		public String get$ref() {
			return $ref;
		}

		public void set$ref(String $ref) {
			this.$ref = $ref;
		}

		public String get$ref() {
			return $ref;
		}

		public void set$ref(String $ref) {
			this.$ref = $ref;
		}

		public String get$ref() {
			return $ref;
		}

		public void set$ref(String $ref) {
			this.$ref = $ref;
		}
	}

	public static class Size {
		private String type;
		private String format;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Total {
		private String type;
		private String format;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionFeedback对象 {
		private String type;
		private Properties properties;
		private String title;
		private String description;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class AttachmentList {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Items items;
		private String refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}

		public String getRefType() {
			return refType;
		}

		public void setRefType(String refType) {
			this.refType = refType;
		}
	}

	public static class AuditName {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class AuditPhoto {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class AuditSiebleUid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class BaseFlag {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class ClamantLevel {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class CreateDate {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class DetailDtoList {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Items items;
		private String refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}

		public String getRefType() {
			return refType;
		}

		public void setRefType(String refType) {
			this.refType = refType;
		}
	}

	public static class FeedbackDuration {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class ImportantLevel {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionCategory {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private ArrayList<String> enum;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public ArrayList<String> getEnum() {
			return enum;
		}

		public void setEnum(ArrayList<String> enum) {
			this.enum = enum;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionDesc {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionPhoto {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionStatus {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private ArrayList<String> enum;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public ArrayList<String> getEnum() {
			return enum;
		}

		public void setEnum(ArrayList<String> enum) {
			this.enum = enum;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class RecommendDesc {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class RecommendFlag {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class RecordList {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Items items;
		private String refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}

		public String getRefType() {
			return refType;
		}

		public void setRefType(String refType) {
			this.refType = refType;
		}
	}

	public static class SaleName {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class SaleSiebleUid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class StoreName {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class StoreRegion {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class StoreSiebleUid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Uid {
		private String type;
		private String format;
		private Object refType;
		private String type;
		private String format;
		private Object refType;
		private String type;
		private String format;
		private Object refType;
		private String type;
		private String format;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class UpdateDate {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class VisitUid {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionFeedbackPortalFile对象 {
		private String type;
		private Properties properties;
		private String title;
		private String description;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class FileId {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class FileName {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class FileSize {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class FileType {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Flag {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionUid {
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;
		private String type;
		private String format;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Url {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionFeedbackDetail对象 {
		private String type;
		private Properties properties;
		private String title;
		private String description;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class AuditDesc {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class AuditRecommendDesc {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class DetailOpType {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class DiscardReason {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class FeedbackDesc {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class PortalNo {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class PortalProcessorSiebleUid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class PortalUid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class ProofDesc {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class ProofPhoto {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionResult {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class ReturnReason {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class QuestionFeedbackPortalLog对象 {
		private String type;
		private Properties properties;
		private String title;
		private String description;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class Createdate {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Instid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Processname {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Processnamecn {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Processnodename {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Reason {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Result {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Startdate {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Status {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Usercnname {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Userid {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Username {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public static class Waitdate {
		private String type;
		private String description;
		private boolean allowEmptyValue;
		private Object refType;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}

		public void setAllowEmptyValue(boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}

		public Object getRefType() {
			return refType;
		}

		public void setRefType(Object refType) {
			this.refType = refType;
		}
	}

	public String getSwagger() {
		return swagger;
	}

	public void setSwagger(String swagger) {
		this.swagger = swagger;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public ArrayList<Object> getSchemes() {
		return schemes;
	}

	public void setSchemes(ArrayList<Object> schemes) {
		this.schemes = schemes;
	}

	public ArrayList<String> getConsumes() {
		return consumes;
	}

	public void setConsumes(ArrayList<String> consumes) {
		this.consumes = consumes;
	}

	public ArrayList<String> getProduces() {
		return produces;
	}

	public void setProduces(ArrayList<String> produces) {
		this.produces = produces;
	}

	public Paths getPaths() {
		return paths;
	}

	public void setPaths(Paths paths) {
		this.paths = paths;
	}

	public Definitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}

