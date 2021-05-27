import UIKit
import KMUnitTestIssueShared

final class SampleViewController: UIViewController {
    @IBOutlet weak var textLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        textLabel.text = CommonKt.createApplicationScreenMessage()
    }
}
