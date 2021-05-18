import { Model } from "./model"
import { BehaviorSubject, Observable} from "rx"

import { School } from "./school/school"

class Store {
    private subject = new BehaviorSubject<Model>(new Model())

    set schools(schools: School[]) {
        this.next({...this.state, schools})
    }
    set school(school: School) {
        this.next({
            ...this.state,
            schools: this.state.schools.filter(s => s.id != school.id).concat(school).sort((l, r) => l.id - r.id)
        })
    }
    set currentSchoolId(id: number) {
        this.next({...this.state, currentSchoolId: id})
    }
    set state(_notAllowed: Model) {
        throw new Error("state is read only")
    }
    get state() {
        return this.subject.getValue()
    }
    get model(): Observable<Model> {
        return this.subject
    }

    private next(state: Model) {
        this.subject.onNext(state)
    }
}
export default new Store()

