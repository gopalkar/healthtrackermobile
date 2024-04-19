package ie.setu.healthtracker.models

interface ActivityStore {
    fun findAll(): List<ActivityModel>
    fun create(activity: ActivityModel)
    fun update(activity: ActivityModel)
    fun delete(activity: ActivityModel)
    fun findById(tag: Long): ActivityModel?
}